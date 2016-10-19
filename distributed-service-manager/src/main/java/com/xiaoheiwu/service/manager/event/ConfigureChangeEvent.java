package com.xiaoheiwu.service.manager.event;

import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.IServiceConfigure;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;

public class ConfigureChangeEvent extends Event{
	public static final String CONFIGURE_CHANGE_EVENT="CONFIGURE_CHANGE_EVENT";
	private IServiceConfigure configure;
	private String serviceName;
	private String nodeIndentity;
	public ConfigureChangeEvent(IServiceConfigure configure,String serviceName,String nodeIndentity ) {
		super(CONFIGURE_CHANGE_EVENT);
		this.configure=configure;
		this.serviceName=serviceName;
		this.nodeIndentity=nodeIndentity;
	}
	
	public IServiceConfigure getConfigure() {
		return configure;
	}
	

	public String getNodeIndentity() {
		return nodeIndentity;
	}

	public String getServiceName() {
		return serviceName;
	}

	public IServiceNode getServiceNode(ServiceConfigureKey key){
		return configure.getServiceNode(serviceName, nodeIndentity);
	}
}
