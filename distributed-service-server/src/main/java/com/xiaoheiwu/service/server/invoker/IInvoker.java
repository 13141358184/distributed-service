package com.xiaoheiwu.service.server.invoker;

import java.util.concurrent.ExecutorService;

import com.xiaoheiwu.service.server.protocol.IServerRequest;

public interface IInvoker {

	public void invoke(IServerRequest request);
	
	public Object getServiceImpl();
	
	public ExecutorService getExecutor();
}
