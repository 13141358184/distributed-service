package com.xiaoheiwu.service.server.service.impl;


import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IServiceManager;
import com.xiaoheiwu.service.manager.configure.IServiceConfigure;
import com.xiaoheiwu.service.manager.event.NodeChangeEvent;
import com.xiaoheiwu.service.manager.event.ServiceChangeEvent;
import com.xiaoheiwu.service.server.invoker.IInvoker;
import com.xiaoheiwu.service.server.service.IServiceRegistion;

@Repository
public class ServiceRegistion extends ConcurrentHashMap<String, IInvoker> implements IServiceRegistion{
	private Logger logger=Logger.getLogger(this.getClass());
	protected IServiceManager serviceManager = ComponentProvider.getInstance(IServiceManager.class);
	private IServiceConfigure serviceConfigure=ComponentProvider.getInstance(IServiceConfigure.class);
	public ServiceRegistion(){
		Event.addListerner(NodeChangeEvent.NODE_CHANGE_EVENT, (IListerner)serviceConfigure);
		Event.addListerner(ServiceChangeEvent.SERVICE_CHANGE_EVENT, (IListerner)serviceConfigure);
	}

	@Override
	public IInvoker  getInvoker(String name) {
		return this.get(name);
		
	}
	@Override
	public void registe(String serviceName, IInvoker invoker) {
		logger.info("注册了新服务："+serviceName);
		this.put(serviceName, invoker);
	}

	
	

}
