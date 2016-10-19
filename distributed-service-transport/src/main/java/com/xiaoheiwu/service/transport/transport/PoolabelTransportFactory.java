package com.xiaoheiwu.service.transport.transport;


import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

import org.apache.commons.pool.PoolableObjectFactory;

import com.xiaoheiwu.service.transport.ITransportArgs;
import com.xiaoheiwu.service.transport.ITransportCreator;


public class PoolabelTransportFactory  implements PoolableObjectFactory<Channel>{
	private ITransportCreator creator;
	private ITransportArgs args;
	public PoolabelTransportFactory(ITransportCreator creator, ITransportArgs args){
		this.creator=creator;
		this.args=args;
	}
	
	@Override
	public void activateObject(Channel arg0) throws Exception {
		
	}
	@Override
	public void destroyObject(Channel arg0) throws Exception {
		if(arg0!=null)arg0.close();
	}
	@Override
	public Channel makeObject() throws Exception {
		return creator.createChannel(args);
	}
	@Override
	public void passivateObject(Channel arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validateObject(Channel arg0) {
		if(arg0==null)return false;
		if(!SocketChannel.class.isInstance(arg0))return false;
		return ((SocketChannel)arg0).isConnected();
	}
	
}
