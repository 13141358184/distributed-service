package com.xiaoheiwu.service.router;

import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.transport.ITransport;


public interface ILocalServer {

	/**
	 * 这个服务是否包含要访问的服务
	 * @param serviceName 服务名称
	 * @return true包含；false不包含
	 */
	public boolean containService(String serviceName);
	
	
	public void invokeCall(IServiceRequest request, ITransport transport);
	
	public int getPort();
	
	
}
