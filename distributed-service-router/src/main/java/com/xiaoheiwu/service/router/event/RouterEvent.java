package com.xiaoheiwu.service.router.event;

import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.protocol.IServiceRequest;

public class RouterEvent extends Event{
	public static final String EVENT_TYPE="ROUTE_EVENT";
	private IServiceRequest serviceRequest;
	private IServiceNode serviceNode;
	public RouterEvent(IServiceRequest serviceRequest,IServiceNode serviceNode) {
		super(EVENT_TYPE);
		this.serviceNode=serviceNode;
		this.serviceRequest=serviceRequest;
	}
	public IServiceRequest getServiceRequest() {
		return serviceRequest;
	}
	public IServiceNode getServiceNode() {
		return serviceNode;
	}

}
