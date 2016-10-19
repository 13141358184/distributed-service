package com.xiaoheiwu.service.manager.governance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.manager.IServiceManager;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.context.ApplicationContext;

public abstract  class AbstractServiceGovernance <T extends IRequestAccess<String, R>,  R> implements IServiceGovernance<R>{
	protected ConcurrentMap<String, Map<String, T>> configures=new ConcurrentHashMap<String, Map<String, T>>();
	protected IServiceManager serviceManager=ComponentProvider.getInstance(IServiceManager.class);
	protected Log log=Log.getLogger(this.getClass());

	@Override
	public boolean handle(R request) {
		String serviceName=getSeviceName(request);
		T access=null;
		if(IServiceNode.class.isInstance(request)){
			access=getRequestAccess(serviceName,(IServiceNode) request);
		}else{
			access=getRequestAccess(serviceName);
		}
		
		if(access!=null&&!access.access(request)){
			doRejectHandle(request);
			return false;
		}
		return true;
	}
	
	

	@Override
	public void updateConfigureInfo(String serviceName, IServiceNode node) {
		T setter=getRequestAccess(serviceName, node);
		String value=node.getConfigure(getConfigureKey().name());
		if(value!=null){
			setter.setValue(value);
		}
	}

	/**
	 * 
	 * @return 创建一个access的子类实现
	 */
	protected T createRequestAccess(String serviceName){
		
		return createRequestAccess(serviceName, null);
	}

	protected IServiceNode getServiceNode(String serviceName, IServiceNode node){
		if(node==null){
			node=ApplicationContext.getInstance().getServiceNode(serviceName);
			node=serviceManager.getSeviceNode(serviceName, node.getIdentity());
		}
		return node;
	}
	protected T createRequestAccess(String serviceName, IServiceNode node){
		node=getServiceNode(serviceName, node);
		T t= newRequestAccess(serviceName, node);
		String key=getConfigureKey().name();
		String value=node.getConfigure(key);
		if(value!=null)t.setValue(value);
		return t;
	}

	protected abstract T newRequestAccess(String serviceName, IServiceNode node);



	/**
	 * 
	 * @param 获取服务name
	 * @return
	 */
	protected abstract String getSeviceName(R request);
	
	/**
	 * 
	 * @param request 如果请求被拒绝，应该做的工作 
	 */
	protected  abstract void doRejectHandle(R request);
	
	
	protected  T getRequestAccess(String serviceName){
		return getRequestAccess(serviceName, null);
	}
	protected  T getRequestAccess(String serviceName, IServiceNode request) {
		Map<String, T> requestAccesses=this.configures.get(serviceName);
		if(requestAccesses==null){
			requestAccesses=createRequestAccessMap(serviceName);
		}
		IServiceNode node=getServiceNode(serviceName, request);
		T t=requestAccesses.get(node.getIdentity());
		if(t==null){
			t=createRequestAccess(serviceName, request);
			requestAccesses.put(node.getIdentity(), t);
		}
		return t;
	}



	private Map<String, T> createRequestAccessMap(String serviceName) {
		Map<String, T> requestAccesses=new HashMap<String, T>();;
		synchronized (this) {
			 requestAccesses=this.configures.get(serviceName);
			 if(requestAccesses==null){
				 requestAccesses=new HashMap<String, T>();
				 configures.put(serviceName, requestAccesses);
			 }
		}
		return requestAccesses;
	}

}
