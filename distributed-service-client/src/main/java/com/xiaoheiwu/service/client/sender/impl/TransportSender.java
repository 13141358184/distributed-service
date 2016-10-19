package com.xiaoheiwu.service.client.sender.impl;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.protocol.impl.ClientRequest;
import com.xiaoheiwu.service.client.router.IRouter;
import com.xiaoheiwu.service.client.router.impl.SelectorRouter;
import com.xiaoheiwu.service.client.sender.ISender;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.IProtocolService;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.transport.ITransport;

/**
 * 把发送的信息放入transaction中，进行发送。
 * @author Chris
 *
 */
@Service
public class TransportSender implements ISender{
	private IRouter transaction=ComponentProvider.getInstance(IRouter.class);
	public TransportSender(){
	}
	@Override
	public boolean sendServiceCall(IClientRequest serviceCall) {

		ITransport transport=getTransport(serviceCall);
		transaction.sendAndReceive(transport, serviceCall, serviceCall.getResponse(), supportChangeTrasport(serviceCall));
		return true;
	}
	
	
	
	private boolean supportChangeTrasport(IClientRequest serviceCall) {
		ITransport transport=serviceCall.getTransport();
		if(transport!=null){
			return true;
		}
		return false;
	}
	private ITransport getTransport(IClientRequest serviceCall) {
		if(supportChangeTrasport(serviceCall)){
			return serviceCall.getTransport();
		}
		ITransport transport= transaction.getTransport(serviceCall);
		serviceCall.setTransport(transport);
		return transport;
	}

	

}
