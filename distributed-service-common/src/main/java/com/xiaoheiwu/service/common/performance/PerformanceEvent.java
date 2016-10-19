package com.xiaoheiwu.service.common.performance;

import com.xiaoheiwu.service.common.event.impl.Event;

public class PerformanceEvent extends Event{
	protected String serviceName;
	protected String methodName;
	protected long callId;
	protected String enventType;
	public PerformanceEvent(String event) {
		super(event);
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public long getCallId() {
		return callId;
	}
	public String getEnventType() {
		return enventType;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public void setCallId(long callId) {
		this.callId = callId;
	}
	public void setEnventType(String enventType) {
		this.enventType = enventType;
	}
	
}
