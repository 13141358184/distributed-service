package com.xiaoheiwu.service.client.protocol;

import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.transport.ITransport;

public interface IClientRequest extends IServiceRequest{

	public void setSendTime(long time);//set send time
	
	public int incRetryCount();// add retry count
	
	
	public IClientResponse getResponse();
	
	public int getRetryCount();
	
	public long getSendTime();
	
	public ITransport getTransport();
	
	public void setTransport(ITransport transport);
	
	public IConfigure getConfigure();
}
