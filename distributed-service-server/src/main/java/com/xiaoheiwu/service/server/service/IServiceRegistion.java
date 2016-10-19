package com.xiaoheiwu.service.server.service;

import com.xiaoheiwu.service.server.invoker.IInvoker;

public interface IServiceRegistion {
	
	public void registe(String serviceName, IInvoker invoker) ;

	
	public IInvoker getInvoker(String name) ;
	
	
}
