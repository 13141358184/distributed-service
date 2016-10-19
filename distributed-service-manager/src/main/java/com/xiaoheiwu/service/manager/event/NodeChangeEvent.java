package com.xiaoheiwu.service.manager.event;

import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IServiceNode;

public class NodeChangeEvent extends Event{
	
	public static final String NODE_CHANGE_EVENT="NODE_CHANGE_EVENT";
	private String path;
	private String serviceName;
	private IServiceNode serverNode;
	public NodeChangeEvent(String path,String serviceName,IServiceNode serverNode) {
		super(NODE_CHANGE_EVENT);
		this.path=path;
		this.serviceName=serviceName;
		this.serverNode=serverNode;
	}
	public String getPath() {
		return path;
	}
	public String getServiceName() {
		return serviceName;
	}
	public IServiceNode getServerNode() {
		return serverNode;
	}
	
}
