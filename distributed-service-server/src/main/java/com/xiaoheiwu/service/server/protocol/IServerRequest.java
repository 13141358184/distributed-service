package com.xiaoheiwu.service.server.protocol;


import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.transport.ITransport;

public interface IServerRequest extends IServiceRequest{
	
	public String getClientIp();
	
	public int getClientPort();
	
	public String getServerIp();
	
	public int getServerPort();

	public ITransport getTransport();
	
	public void sendSuccessResponse(Object object);
	
	public void sendErrorResponse(ResponseCode responseCode,String errorDescription);
	
	public void sendErrorResponse(String errorDescription);
	
	public IRouterManager getRouter();
	
	public CallChain getCallChain();
	
	public boolean isJVMCall();
}
