package com.xiaoheiwu.service.protocol.serializer;

import com.xiaoheiwu.service.protocol.impl.ServiceResponse;

public class ReturnInfo {
	private long serviceId;
	private Object returnObject;
	private int code;
	private String description;
	public ReturnInfo(ServiceResponse return1){
		this.serviceId=return1.getServiceCallId();
		this.returnObject=return1.getReturnObject();
		this.code=return1.getCode();
		this.description=return1.getDescription();
	}
	public long getServiceId() {
		return serviceId;
	}
	public Object getReturnObject() {
		return returnObject;
	}
	public int getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
