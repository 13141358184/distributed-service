package com.xiaoheiwu.service.client.protocol.impl;

import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.client.receiver.IReceiver;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;

public class ClientResponse extends ServiceResponse implements IClientResponse{
	private volatile ResponseCode responseCode=ResponseCode.NONE;
	private final String serviceName;
	private volatile IReceiver receiver;
	private final String methodName;
	public ClientResponse(long serviceCallId, String serviceName, String methodName){
		this.serviceCallId=serviceCallId;
		this.serviceName=serviceName;
		this.methodName=methodName;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public IReceiver getReceiver() {
		return receiver;
	}
	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.setCode(this.responseCode.getCode());
	}

	
	public void setReceiver(IReceiver receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public String getMethodName() {
		return this.methodName;
	}

	@Override
	public void finishResponse(IServiceResponse returnValue) {
		ResponseCode code=ResponseCode.getResponseCode(returnValue.getCode());
		String description=returnValue.getDescription();
		Object object=returnValue.getReturnObject();
		finishedResponse(code,description,object);
	}

	@Override
	public void finishedResponse(ResponseCode code, String responseDescription,
			Object returnObject) {
		super.finishedResponse(code, responseDescription, returnObject);
		IReceiver receiver=this.getReceiver();
		receiver.doRecieve(this);
	}


	

}
