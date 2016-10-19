package com.xiaoheiwu.service.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ResponseCode;

public class ServiceResponse implements IServiceResponse{
	private volatile boolean isFinished=false;
	protected long serviceCallId;
	protected Object returnObject;
	protected int code;
	protected String description;
	protected Map<String, Object> additionalParameters=new HashMap<String, Object>();
	public long getServiceCallId() {
		return serviceCallId;
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
	public Map<String, Object> getAdditionalParameters() {
		return additionalParameters;
	}
	public void setServiceCallId(long serviceId) {
		this.serviceCallId = serviceId;
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
	public void setAdditionalParameters(Map<String, Object> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}
	public void setResponseCode(ResponseCode code2) {
		this.setCode(code2.getCode());
	}
	public void setFinished(boolean b) {
		this.isFinished=b;
	}
	public void setIsFinished(boolean b){
		this.setFinished(b);
	}
	public boolean getIsFinished(){
		return isFinished();
	}
	public boolean isFinished() {
		return isFinished;
	}
	
	@Override
	public void finishedResponse(ResponseCode code, String responseDescription,
			Object returnObject) {
		this.setFinished(true);
		this.setResponseCode(code);
		this.setDescription(responseDescription);
		this.returnObject=returnObject;
	}

	
	@Override
	public IServiceResponse createSerializableResponse() {
		ServiceResponse response=new ServiceResponse();
		response.setAdditionalParameters(getAdditionalParameters());
		response.setCode(getCode());
		response.setDescription(getDescription());
		response.setFinished(isFinished());
		response.setReturnObject(getReturnObject());
		return response;
	}
	@Override
	public ResponseCode getResponseCode() {
		return ResponseCode.getResponseCode(code);
	}
	
}
