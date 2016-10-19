package com.xiaoheiwu.service.client.protocol.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;
import com.xiaoheiwu.service.transport.ITransport;

/**
 * 封装方法调用需要的信息
 * @author Chris
 *
 */
public class ClientRequest extends ServiceRequest implements IClientRequest{
	private Method method;
	private IClientResponse response;
	private ITransport transport;
	private AtomicInteger retryCount=new AtomicInteger();
	private long sendTime=0;
	private CallChain callChain;
	private IConfigure configure;
	public ClientRequest(String serviceInterfaceName,Method method,Object[] parameters,IClientResponse response,CallChain callChain){
		this.serviceName=serviceInterfaceName;
		this.methodName=method.getName();
		this.method=method;
		this.parameters=parameters;
		this.response=response;
		this.serviceCallId=response.getServiceCallId();
		this.callChain=callChain;
		additionalParameters.put(CallChain.class.getSimpleName(), getCallChain());
	}


	public void finishResponse(ServiceResponse returnValue, IClientResponse response) {
		ResponseCode code=ResponseCode.getResponseCode(returnValue.getCode());
		String description=returnValue.getDescription();
		Object object=returnValue.getReturnObject();
		response.finishedResponse(code,description,object);
	}
	public Method getMethod() {
		return method;
	}
	public Object[] getParameters() {
		return parameters;
	}

	public IClientResponse getResponse() {
		return response;
	}
	public int getRetryCount() {
		return retryCount.get();
	}
	public int incRetryCount() {
		return this.retryCount.incrementAndGet();
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public long getSendTime() {
		return sendTime;
	}
	public ITransport getTransport() {
		return transport;
	}
	public void setTransport(ITransport transport) {
		this.transport = transport;
	}
	public CallChain getCallChain() {
		return callChain;
	}
	public IConfigure getConfigure() {
		return configure;
	}
	public void setConfigure(IConfigure configure) {
		this.configure = configure;
	}


	public void setCallChain(CallChain callChain) {
		this.callChain = callChain;
		additionalParameters.put(CallChain.class.getSimpleName(), getCallChain());
	}
	
}
