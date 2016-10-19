package com.xiaoheiwu.service.server.handle.requesthandle;


import org.springframework.beans.factory.annotation.Autowired;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.component.Components;
import com.xiaoheiwu.service.server.handle.IRequestHandle;
import com.xiaoheiwu.service.server.invoker.IInvoker;
import com.xiaoheiwu.service.server.protocol.IServerRequest;
import com.xiaoheiwu.service.server.service.IServiceRegistion;
import com.xiaoheiwu.service.server.service.impl.ServiceRegistion;
/**
 * 执行服务的方法调用,并返回结果
 * @author Chris
 *
 */
public class InvokeRequestHandle implements IRequestHandle{
	@Autowired
	private IServiceRegistion serviceRegistion=Components.getInstance().getBean(IServiceRegistion.class);
	@Override
	public boolean handle(IServerRequest request) {
		IInvoker invoker=getService(request);
		if(invoker==null){
			request.sendErrorResponse("没有找到"+request.getServiceName()+"的实例");
			return false;
		}
	
		invoker.invoke(request);
		
		return true;
	}


	private IInvoker getService(IServerRequest request) {
		String serviceName=request.getServiceName();
		IInvoker service=this.serviceRegistion.getInvoker(serviceName);
		return service;
	}



}
