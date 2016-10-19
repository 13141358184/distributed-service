package com.xiaoheiwu.service.server;

import java.nio.channels.SelectionKey;







import com.xiaoheiwu.service.server.event.SystemInitEvent;
import com.xiaoheiwu.service.transport.IServerTransport;
import com.xiaoheiwu.service.transport.ITransportFactory;
import com.xiaoheiwu.service.transport.selector.ISelector;
import com.xiaoheiwu.service.transport.selector.impl.ServiceSelector;
import com.xiaoheiwu.service.transport.transport.TransportFactory;

public class NonBlockServer implements IServer{
	protected ITransportFactory transportFactory=TransportFactory.getInstance();
	protected ISelector selector;
	protected IServerTransport serverTransport=null;
	public NonBlockServer(ISelector selector){
		this.selector=selector;
		serverTransport=transportFactory.openServerTransport(); 
	}
	public NonBlockServer(){
		serverTransport=transportFactory.openServerTransport();
		this.selector=new ServiceSelector();
	}
	
	
	@Override
	public void start(int port) {
		try{ 
			serverTransport.configureBlocking(false);
			//绑定端口
			serverTransport.bind(port);
			serverTransport.setTimeout(0);
	        selector.register(serverTransport, SelectionKey.OP_ACCEPT);
	        
	        new SystemInitEvent(selector, serverTransport, port).fireEvent();
		}catch(Exception exception){
			exception.printStackTrace();
		}
		
	}


	@Override
	public void stop() {
		selector.stop();
	}

}
