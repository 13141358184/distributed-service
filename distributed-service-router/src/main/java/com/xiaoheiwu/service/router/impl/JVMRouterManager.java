package com.xiaoheiwu.service.router.impl;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.router.ILocalServer;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.IRouterReceiver;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.IFrame;

public class JVMRouterManager extends AbstractRouterManager implements IRouterManager{
	private static final JVMRouterManager instance=new JVMRouterManager();
	private JVMRouterManager(){}
	public static IRouterManager getInstance(){
		return instance;
	}
	private ConcurrentMap<Long, IRouterReceiver> receivers=new ConcurrentHashMap<>();
	private ILocalServer localServer=ComponentProvider.getInstance(ILocalServer.class);
	@Override
	public IFrame send(ITransport transport, IServiceRequest request,
			IRouterReceiver receiver) throws IOException {
		
		if(!isJVMRouter(request))throw new RuntimeException("不能进行本地方法调用，请用运程调用。"+request.toString());
		localServer=ComponentProvider.getInstance(ILocalServer.class);
		registeReceiver(request.getServiceCallId(),receiver);
		localServer.invokeCall(request,getTransport(request));
		return null;
	}

	

	@Override
	public IFrame send(ITransport transport, IServiceResponse response)
			throws IOException {
		Long serviceCallId=response.getServiceCallId();
		IRouterReceiver receiver=receivers.remove(serviceCallId);
		if(receiver!=null)receiver.receive(response);
		logger.error("没有找到注册receiver，调用失败。"+response.toString());
		return null;
	}

	@Override
	public ITransport getTransport(IServiceRequest request) {
		return factory.getLocalTransport(localServer.getPort());
	}
	
	private void registeReceiver(long serviceCallId, IRouterReceiver receiver) {
		this.receivers.putIfAbsent(serviceCallId,receiver);
	}
}
