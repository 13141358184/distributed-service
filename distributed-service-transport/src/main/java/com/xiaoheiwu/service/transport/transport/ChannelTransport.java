package com.xiaoheiwu.service.transport.transport;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.commons.pool.impl.GenericObjectPool;

import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.ITransportFactory;


public class ChannelTransport  implements ITransport{

	private ByteChannel channel;
	private GenericObjectPool<Channel> pool;
	private ITransportFactory transportFactory;
	private long lastUpdateTime=-1;
	public ChannelTransport(Channel socketChannel,GenericObjectPool<Channel> pool){
		this.channel=(ByteChannel)socketChannel;
		this.pool=pool;
		this.lastUpdateTime=System.currentTimeMillis();
	}
	public ChannelTransport(Channel socketChannel){
		this(socketChannel, null);
	}

	public void close() throws IOException {
		try {
//			if(pool!=null){
//				pool.returnObject(channel);
//				lastUpdateTime=-1;
//				return;
//			}
			channel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int read(ByteBuffer arg0) throws IOException {
		lastUpdateTime=System.currentTimeMillis();
		return channel.read(arg0);
	}

	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}

	@Override
	public int write(ByteBuffer arg0) throws IOException {
		lastUpdateTime=System.currentTimeMillis();
		return channel.write(arg0);
	}



	
	public Channel getChannel() {
		return channel;
	}
	@Override
	public boolean isSelectable() {
		if(SelectableChannel.class.isInstance(channel)){
			return true;
		}
		return false;
	}
	@Override
	public void configureBlocking(boolean blocking) {
		if(SelectableChannel.class.isInstance(this.channel)){
			SelectableChannel selectableChannel=(SelectableChannel)channel;
			try {
				selectableChannel.configureBlocking(blocking);
				return ;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("通道不支持配置blocking");
		
		
	}
	@Override
	public SelectionKey register(Selector selector, int opts) {
		if(SelectableChannel.class.isInstance(this.channel)){
			SelectableChannel selectableChannel=(SelectableChannel)channel;
			try {
				return selectableChannel.register(selector, opts);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("通道不支持配置blocking");
	}


	
	public void setTransportFactory(
			ITransportFactory transportFactory) {
		this.transportFactory=transportFactory;
	}
	@Override
	public boolean timeout(long timeout) {
		long gap=System.currentTimeMillis()-this.lastUpdateTime;
		if(gap/1000>timeout)return true;
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(channel.toString()+";");
		if(SocketChannel.class.isInstance(channel)){
			SocketChannel socket=(SocketChannel)channel;
			try {
				sb.append(socket.getRemoteAddress().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
