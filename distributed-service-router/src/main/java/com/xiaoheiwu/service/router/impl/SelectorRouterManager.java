package com.xiaoheiwu.service.router.impl;

import java.nio.channels.SelectionKey;

import org.springframework.stereotype.Service;


import com.xiaoheiwu.service.router.IRouterReceiver;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.Frame;
import com.xiaoheiwu.service.transport.frame.IFrame;
import com.xiaoheiwu.service.transport.selector.ISelector;
import com.xiaoheiwu.service.transport.selector.SelectorFactory;

@Service
public class SelectorRouterManager extends AbstractRouterManager{
	public static final String PROVIDER_NAME="SelectorTransaction";
	private ISelector selector=SelectorFactory.getInstance().createWriteSelector();
	
	public SelectorRouterManager(){
	} 


	protected void registeSelect(ITransport transport, int opts, IFrame frame) {
		this.selector.register(transport, opts, frame);
	}

	@Override
	protected IFrame send(ITransport transport, byte[] data,
			IRouterReceiver receiver) {
		Frame frame=new Frame(transport);
		frame.write(data);
		frame.setAttach( receiver);
		registeSelect(transport,SelectionKey.OP_WRITE,frame);	
		return frame;
	}
	
}
