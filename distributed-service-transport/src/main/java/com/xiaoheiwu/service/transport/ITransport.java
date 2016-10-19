package com.xiaoheiwu.service.transport;

import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;


public interface ITransport extends ByteChannel{
	
	public Channel getChannel();
	
	public boolean isSelectable();
	
	public void configureBlocking(boolean blocking);
	
	public SelectionKey register(Selector selector, int opts);
	
	/**
	 * 如果transport从创建到后timeout内没有任何使用，则会产生超时
	 * @param timeout 需要的超时时间
	 * @return
	 */
	public boolean timeout(long timeout);
	
	public boolean isOpen();
}
