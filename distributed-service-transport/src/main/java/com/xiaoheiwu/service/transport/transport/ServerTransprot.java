package com.xiaoheiwu.service.transport.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.xiaoheiwu.service.transport.IServerTransport;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.ITransportFactory;

public class ServerTransprot implements IServerTransport {
	private ServerSocketChannel serverSocketChannel;
	private ITransportFactory transportFactory;
	public ServerTransprot(){
		initServerSocketChannel();
	}
	
	public Channel getChannel() {
		return serverSocketChannel;
	}

	@Override
	public boolean isSelectable() {
		return SelectableChannel.class.isInstance(serverSocketChannel);
	}

	@Override
	public void configureBlocking(boolean blocking) {
		try {
			serverSocketChannel.configureBlocking(blocking);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SelectionKey register(Selector selector, int opts) {
		try {
			return this.serverSocketChannel.register(selector, opts);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		throw new RuntimeException("不支持读操作");
	}

	@Override
	public boolean isOpen() {
		return serverSocketChannel.isOpen();
	}

	@Override
	public void close() throws IOException {
		this.serverSocketChannel.close();
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		throw new RuntimeException("不支持写操作");
	}

	@Override
	public ITransport accept() {
		try {
			SocketChannel socketChannel=this.serverSocketChannel.accept();
			ITransport transport=new ChannelTransport(socketChannel);
			return transport;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void initServerSocketChannel(){
		try {
			serverSocketChannel=ServerSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void bind(int port) {
		try {
			this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setTimeout(int timeout) {
		try {
			this.serverSocketChannel.socket().setSoTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	
	public void setTransportFactory(ITransportFactory transportFactory) {
		this.transportFactory=transportFactory;
	}

	@Override
	public boolean timeout(long timeout) {
		return false;
	}

}
