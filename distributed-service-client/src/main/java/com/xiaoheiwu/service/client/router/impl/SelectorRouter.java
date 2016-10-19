package com.xiaoheiwu.service.client.router.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.client.router.IRouter;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.IRouterReceiver;
import com.xiaoheiwu.service.transport.ITransport;
@Service
@ServiceDefault(IRouter.class)
public class SelectorRouter implements IRouter{
	private IRouterManager routerManager=ComponentProvider.getInstance(IRouterManager.class);
	@Override
	public void sendAndReceive(ITransport transport, IClientRequest request,  final IClientResponse response, boolean supportChangeTrasport) {
			
		try {
			routerManager.send(transport,  request,  new IRouterReceiver() {
				
			
				@Override
				public void receive(IServiceResponse returnValue) {
					ResponseCode code=returnValue.getResponseCode();
					String description=returnValue.getDescription();
					Object returnObject=returnValue.getReturnObject();
					getServiceResponse().finishedResponse(code, description, returnObject);
				}

				@Override
				public IServiceResponse getServiceResponse() {
					return response;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			response.finishedResponse(ResponseCode.LOCAL_EXCEPTION, e.getMessage(), null);
		}
		
	}

	

	@Override
	public ITransport getTransport(IClientRequest serviceCall) {
		try{
			ITransport transport= routerManager.getTransport(serviceCall);
			return transport;
		}catch(Exception e){
			e.printStackTrace();
			serviceCall.getResponse().finishedResponse(ResponseCode.LOCAL_EXCEPTION, e.getMessage(), null);
			return null;
		}
		
	}



	private boolean isInJVM() {
		return false;
	}
	
}
