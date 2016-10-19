package com.xiaoheiwu.service.client.event;

import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.common.performance.PerformanceEvent;

public class CallEvent extends PerformanceEvent{
	private IClientResponse response;
	public CallEvent(String event, IClientResponse response) {
		super(event);
		this.response=response;
		this.setCallId(response.getServiceCallId());
		this.setEnventType(String.valueOf(this.getEvent()));
		this.setMethodName(response.getMethodName());
		this.setServiceName(response.getServiceName());
	}
	public IClientResponse getResponse() {
		return response;
	}
	
	public static void fireEvent(ClientEventType event,IClientResponse response){
		CallEvent callEvent=new CallEvent(event.getEvent(), response);
		callEvent.fireEvent();
	}

}
