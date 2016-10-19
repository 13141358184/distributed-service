package com.xiaoheiwu.service.container.container;

import com.xiaoheiwu.service.common.lifecycle.ILifecycle;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceNode;


public interface IServiceContainer extends ILifecycle{

	public IServiceNode getServiceNode();
	
	public Class getServiceClass();
	
	public Object getServiceImpl();
	
	public IService getService();
}
