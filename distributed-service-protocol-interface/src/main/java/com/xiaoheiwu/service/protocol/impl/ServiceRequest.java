package com.xiaoheiwu.service.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import com.xiaoheiwu.service.protocol.IServiceRequest;


public class ServiceRequest implements IServiceRequest{
	protected long serviceCallId=0;
	protected byte protocolId=0;
	protected String serviceName;
	protected String methodName;
	protected Object[] parameters;
	protected Map<String, Object> additionalParameters=new HashMap<String, Object>();
	
	@Override
	public long getServiceCallId() {
		return serviceCallId;
	}
	@Override
	public String getServiceName() {
		return serviceName;
	}
	@Override
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	@Override
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	@Override
	public Map<String, Object> getAdditionalParameters() {
		return additionalParameters;
	}
	public void setAdditionalParameters(Map<String, Object> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}
	@Override
	public byte getProtocolId() {
		return protocolId;
	}
	
	public void setServiceCallId(long serviceCallId) {
		this.serviceCallId = serviceCallId;
	}
	public void setProtocolId(byte protocolId) {
		this.protocolId = protocolId;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("serviceId :"+this.serviceCallId+"\r\n");
		sb.append("protocolIndex :"+this.protocolId+"\r\n");
		sb.append("serviceInterfaceName :"+this.serviceName+"\r\n");
		sb.append("methodName :"+this.methodName+"\r\n");
		sb.append("parameters :"+this.parameters.length+"\r\n");
		for(Object object: parameters){
			sb.append("   "+object.toString()+"\r\n");
		}
		return sb.toString();
	}
	@Override
	public IServiceRequest createSerializableRequest() {
		ServiceRequest call=new ServiceRequest();
		call.setMethodName(getMethodName());
		call.setParameters(getParameters());
		call.setProtocolId(getProtocolId());
		call.setServiceCallId(getServiceCallId());
		call.setServiceName(getServiceName());
		call.setAdditionalParameters(additionalParameters);
		return call;
	}
	
}
