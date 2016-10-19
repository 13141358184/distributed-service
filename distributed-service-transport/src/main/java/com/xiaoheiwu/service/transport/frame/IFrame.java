package com.xiaoheiwu.service.transport.frame;

import java.nio.channels.ByteChannel;

import com.xiaoheiwu.service.transport.ITransport;


public interface IFrame {
	public final static int MAX_READ_BYTE_SIZE=Integer.MAX_VALUE;//每帧最大的字节限制
	
	public ITransport getTransport();//可以读写数据的channel

	public int read();//从ITransport中读取数据，返回读取的数据数
	
	public byte[] getDate();//获取一份完整的消息内容，如果消息还未读完会抛出异常，消息保持现状
	
	public boolean readFinish();//判断消息是否读取完毕
	
	public boolean write(byte[] message);//把一份完成的消息写入Frame中，但并未发送到ITransport
	
	public boolean writeAndFlush(byte[] message);//把消息写入Frame中，同时发送到ITransport
	
	public boolean flush();//把数据写入ITransport中
	
	public void close();//清理资源，会关闭和frame相关的所有资源
	
	public Object attach();//允许使用者附加一个附件，在框架层不会对此对象做任何处理。
	
	
	
}
