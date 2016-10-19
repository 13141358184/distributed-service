package com.xiaoheiwu.service.router.impl;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.IRouterReceiver;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.IFrame;
import com.xiaoheiwu.service.transport.handle.IReadHandle;
import com.xiaoheiwu.service.transport.message.IMessage;
import com.xiaoheiwu.service.transport.selector.ISelector;
import com.xiaoheiwu.service.transport.selector.SelectorFactory;
@Service
@ServiceDefault(IRouterManager.class)
public class SenderAndReceiveRouterManager extends AbstractRouterManager implements IReadHandle{

	protected ISelector readSelector=SelectorFactory.getInstance().createReadSelector();
	protected IRouterManager routerManager = ComponentProvider.getInstance(TransportRouterManager.class);
	public SenderAndReceiveRouterManager(){
		readSelector.addReadHandle(this);
	}


	@Override
	public int getHandleType() {
		return IReadHandle.READ;
	}

	@Override
	public boolean handle(IMessage message) {
		Object object=message.attach();
		if(!IRouterReceiver.class.isInstance(object)){
			throw new RuntimeException("attach is error ,can not finish call receiver");
		}
		IRouterReceiver receiver=(IRouterReceiver)object;
		IServiceResponse response=protocolService.doReturn(message.getMessage());
		receiver.receive(response);
		message.close();
		return true;
	}
	protected void registeReadSelector(ITransport transport, IFrame frame) {
		readSelector.register(transport, SelectionKey.OP_READ, frame);
	}


	@Override
	public IFrame send(ITransport transport, IServiceRequest request,
			IRouterReceiver receiver) throws IOException {

		IFrame frame= routerManager.send(transport, request, receiver);
		registeReadSelector(transport, frame);
		return frame;
	}


}
