package com.xiaoheiwu.service.router;

import java.io.IOException;

import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.IFrame;
/**
 * 用何种方式完成数据的传送
 * @author Chris
 *
 */
public interface IRouterManager {
	public static final String ROUTE_FLAG="ROUTE_FLAG";
	public static final String JVM_ROUTE_FLAG="JVM_ROUTE_FLAG";
	public static final String LOCAL_ROUTE_FLAG="LOCAL_ROUTE_FLAG";
	public IFrame send(ITransport transport, IServiceRequest request, IRouterReceiver receive) throws IOException;
	
	public IFrame send(ITransport transport, IServiceResponse response) throws IOException;
	
	public ITransport getTransport(IServiceRequest request);
	
	public boolean isJVMRouter(IServiceRequest request);
	
	public boolean isLocalRouter(IServiceRequest request);
}
