package com.xiaoheiwu.service.transport.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.transport.ITransport;

public class JVMChannelTransport implements ITransport{
	private String serviceName;
	public JVMChannelTransport(String serviceName){
		this.serviceName=serviceName;
	}
	@Override
	public int read(ByteBuffer dst) throws IOException {
		throw new RuntimeException("不支持此方法的调用");
	}

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		ComponentProvider.getInstance("");
		return 0;
	}

	@Override
	public Channel getChannel() {
		return null;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void configureBlocking(boolean blocking) {
	}

	@Override
	public SelectionKey register(Selector selector, int opts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean timeout(long timeout) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

}
