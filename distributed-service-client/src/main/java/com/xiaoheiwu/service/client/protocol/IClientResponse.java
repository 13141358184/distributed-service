package com.xiaoheiwu.service.client.protocol;

import com.xiaoheiwu.service.client.receiver.IReceiver;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;


public interface IClientResponse extends IServiceResponse{

	public String getServiceName();//返回调用的service name
	
	public String getMethodName();//返回调用的service name

	public IReceiver getReceiver();//获得回调处理器
	
	
	
	public void finishResponse(IServiceResponse returnValue);
	
	
}
