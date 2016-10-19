package com.xiaoheiwu.service.server.protocol.impl;

import java.io.IOException;

import java.nio.channels.SocketChannel;

import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.util.IPUtil;

import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.server.protocol.IServerRequest;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.message.IMessage;
public class ServerRequest extends ServiceRequest  implements IServerRequest{
	private IRouterManager routerManager;
	private CallChain callChain;
	private ITransport transport;
	private boolean isJVMCall=false;
	public ServerRequest(IServiceRequest request, ITransport transport,IRouterManager routerManager){
		initRequest(request);
		this.transport=transport;
		this.routerManager=routerManager;
		Object o=getAdditionalParameters().get(CallChain.class.getSimpleName());
		if(o!=null){ 
			this.callChain=(CallChain) o;
		}
	}
	private void initRequest(IServiceRequest request) {
		this.setAdditionalParameters(request.getAdditionalParameters());
		if(IRouterManager.JVM_ROUTE_FLAG.equals(getAdditionalParameters().get(IRouterManager.ROUTE_FLAG))){
			this.isJVMCall=true;
		}
		this.setMethodName(request.getMethodName());
		this.setParameters(request.getParameters());
		this.setProtocolId(request.getProtocolId());
		this.setServiceCallId(request.getServiceCallId());
		this.setServiceName(request.getServiceName());
		
	}

	@Override
	public void sendSuccessResponse(Object object) {
		ServerResponse response=fillResponse(ResponseCode.SUCCESS, "OK", object);
		try {
			routerManager.send(getTransport(),  response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendErrorResponse(String errorDescription) {
		sendErrorResponse(ResponseCode.REMOTO_EXCEPTION, errorDescription);
	}
	@Override
	public void sendErrorResponse(ResponseCode code, String errorDescription) {
		ServerResponse response=fillResponse(code, errorDescription, null);
		try {
			routerManager.send(getTransport(),  response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected ServerResponse fillResponse(ResponseCode code, String description, Object object){
		ServerResponse response=new ServerResponse();
		response.setResponseCode(code);
		response.setDescription(description);
		response.setReturnObject(object);
		response.setServiceCallId(getServiceCallId());
		if(isJVMCall){
			response.getAdditionalParameters().put(IRouterManager.ROUTE_FLAG, IRouterManager.JVM_ROUTE_FLAG);
		}
		return response;
	}
	
	@Override
	public IRouterManager getRouter() {
		return this.routerManager;
	}

	@Override
	public CallChain getCallChain() {
		return this.callChain;
	}
	
	@Override
	public String getClientIp() {
		SocketChannel channel=getSocketChannel();
		return IPUtil.getIp(channel, false);
	}

	@Override
	public int getClientPort() {
		SocketChannel channel=getSocketChannel();
		return IPUtil.getPort(channel, false);
	}
	@Override
	public String getServerIp() {
		SocketChannel channel=getSocketChannel();
		return IPUtil.getIp(channel, true);
		
	}
	
	
	@Override
	public int getServerPort() {
		SocketChannel channel=getSocketChannel();
		return IPUtil.getPort(channel, true);
	
	}

	private SocketChannel getSocketChannel() {
		ITransport transport=getTransport();
		if(transport==null)return null;
		return IPUtil.getSocketChannel(transport.getChannel());
	}
	@Override
	public ITransport getTransport() {
		return transport;
	}
	@Override
	public boolean isJVMCall() {
		return isJVMCall;
	}
}
