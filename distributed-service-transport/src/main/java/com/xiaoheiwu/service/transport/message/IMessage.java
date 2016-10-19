package com.xiaoheiwu.service.transport.message;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;

import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.IFrame;

/**
 * 传输层对外提供的接口，里面是一个完成的消息。半包，粘包 等问题都在下层解决
 * @author Chris
 *
 */
public interface IMessage {

	public byte[] getMessage();
	
	public void writeMessage(byte[] message);
	
	public void close();
	
	public void flush();
	
	public ITransport getTransport();
	
	public Object attach();
	
	public IFrame getFrame();
	
}
