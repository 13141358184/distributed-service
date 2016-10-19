package com.xiaoheiwu.service.server.protocol;

import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ResponseCode;



public interface IServerResponse extends IServiceResponse{
	
	public ResponseCode getResponseCode();
	
	public IServiceResponse createResponse() ;
}
