package com.xiaoheiwu.service.manager.context;


import com.xiaoheiwu.service.common.configure.impl.Configure;
import com.xiaoheiwu.service.manager.IServiceNode;
/**
 * 通用的上下文，整个应用共享。客户端和服务器端分别有一个ApplicationContext
 * @author Chris
 *
 */
public final class ApplicationContext extends Configure{
	private static final ApplicationContext instance=new ApplicationContext();
	private ApplicationContext(){}
	public static ApplicationContext getInstance(){
		return instance;
	}
	
	/**
	 * 根据服务名称获取，本机对应的服务节点，主要用于服务器端
	 * @param serviceName 服务名称
	 * @return
	 */
	public IServiceNode getServiceNode(String serviceName){
		Object value= this.getValue(serviceName);
		if(value==null||!IServiceNode.class.isInstance(value)){
			return null;
		}
		return (IServiceNode) value;
	}
	
	/**
	 * 把服务节点注册到本地上下文中
	 * @param serviceName
	 * @param serviceNode
	 */
	public void setServiceNode(String serviceName, IServiceNode serviceNode){
		this.putValue(serviceName, serviceNode);
	}
}
