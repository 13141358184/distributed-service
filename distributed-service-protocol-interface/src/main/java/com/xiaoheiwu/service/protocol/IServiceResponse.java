package com.xiaoheiwu.service.protocol;

import java.util.Map;

public interface IServiceResponse {

	
	public int getCode();//返回调用的代码，如成功还是失败
	
	public ResponseCode getResponseCode();//返回调用的代码，如成功还是失败
	
	public String getDescription();//返回调用结果的描述，主要针对失败的情况，此处存放失败的描述信息。
	
	public Object getReturnObject();//返回调用的结果对象，如果没有返回值，此处返回null
	
	public long getServiceCallId();//每次调用都会生成一个service call id
	
	public Map<String, Object> getAdditionalParameters();
	
	public boolean isFinished();

	public void finishedResponse(ResponseCode code,String responseDescription, Object returnObject);
	
	public IServiceResponse createSerializableResponse();
	
}
