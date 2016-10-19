package com.xiaoheiwu.service.transport.handle.impl;

import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.transport.IServerTransport;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.handle.IAcceptHandle;
import com.xiaoheiwu.service.transport.handle.IHandle;
import com.xiaoheiwu.service.transport.handle.ISelectorHandle;
import com.xiaoheiwu.service.transport.transport.TransportFactory;

public class AcceptSelectorHandle implements ISelectorHandle{
	protected IServerTransport serverTransport;
	protected IAcceptHandle acceptHandle;
	public AcceptSelectorHandle(IServerTransport serverTransport){
		this.serverTransport=serverTransport;
	}
	@Override
	public int getHandleType() {
		return ISelectorHandle.ACCEPT;
	}

	@Override
	public void handle(SelectionKey key) {
	try {
			ITransport transport=serverTransport.accept();
			acceptHandle.handle(transport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addHandle(IHandle handle) {
		if(handle.getHandleType()!=IHandle.ACCEPT)return false;
		if(!IAcceptHandle.class.isInstance(handle))return false;
		IAcceptHandle acceptHandle=(IAcceptHandle)handle;
		this.acceptHandle=acceptHandle;
		return true;
	}

}
