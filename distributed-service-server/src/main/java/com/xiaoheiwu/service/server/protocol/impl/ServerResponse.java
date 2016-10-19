package com.xiaoheiwu.service.server.protocol.impl;

import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.server.protocol.IServerResponse;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;
import com.xiaoheiwu.service.protocol.IServiceResponse;

public class ServerResponse extends ServiceResponse implements IServerResponse{
	private volatile ResponseCode responseCode=ResponseCode.NONE;
	
	public ResponseCode getResponseCode() {
		return responseCode;
	}
	

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.setCode(responseCode.getCode());
	}
	@Override
	public IServiceResponse createResponse() {
		ServiceResponse value=new ServiceResponse();
		value.setCode(getResponseCode().getCode());
		value.setDescription(getDescription());
		value.setReturnObject(getReturnObject());
		value.setServiceCallId(getServiceCallId());
		return value;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("response code is "+this.responseCode+" ; ");
		if(ResponseCode.SUCCESS==this.responseCode){
			sb.append("response object is \""+this.returnObject.toString()+"\" ; ");
		}else{
			sb.append("response code description is "+this.description+" ; ");
		}
		return sb.toString();
	}
	
}
