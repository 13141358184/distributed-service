package com.xiaoheiwu.service.client.services;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xiaoheiwu.service.annotation.util.ServiceUtils;
import com.xiaoheiwu.service.client.init.SystemInitEvent;
import com.xiaoheiwu.service.client.receiver.IReceiver;
import com.xiaoheiwu.service.manager.version.VersionUtil;
import com.xiaoheiwu.service.transport.ITransport;

public class ServiceFactory {
	private final static ServiceFactory factory=new ServiceFactory();
	private ServiceFactory(){}
	static{
		new SystemInitEvent().fireEvent();
	}
	public static ServiceFactory getInstance(){
		return factory;
	}
	protected ConcurrentMap<String, Object> interfaceCache=new ConcurrentHashMap<String, Object>(); 
	public <T> T getService(Class<T> service){
		return getService(service, null);
	}
	public <T> T getService(Class<T> service,ITransport transport) {
		return getProxy(service, false, null,transport);
	}

	public <T> T getAsynService(Class<T> service, IReceiver receiver){
		return getAsynService(service, receiver, null);
		
	}
	public <T> T getAsynService(Class<T> service, IReceiver receiver,ITransport transport) {
		return getProxy(service, true, receiver,transport);
	}
	protected <T> T getProxy(Class<T> serviceClass, boolean isAsyn, IReceiver receiver,ITransport transport){
		String name=ServiceUtils.getServiceName(serviceClass);
		if(interfaceCache.containsKey(name)){
			return (T)interfaceCache.get(name);
		}
		T proxy=createProxy(serviceClass, isAsyn, receiver,transport);
		this.interfaceCache.putIfAbsent(name, proxy);
		return proxy;
	}
	protected <T> T createProxy(Class<T> service, boolean isAsyn, IReceiver receiver, ITransport transport){
		try{
			String serviceName=ServiceUtils.getServiceName(service);
			String version=ServiceUtils.getVersion(service);
			if(version!=null&&VersionUtil.getVersion(serviceName)==null)VersionUtil.putVersion(serviceName, version);
			return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{service}, new ServiceProxy(serviceName, isAsyn, receiver, transport));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
