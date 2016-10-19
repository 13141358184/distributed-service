package com.xiaoheiwu.service.transport.message;

import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.IFrame;


public class Message implements IMessage{
	private IFrame frame;
	private byte[] message;
	public Message(IFrame frame){
		if(!frame.readFinish())throw new RuntimeException("未读完的信息不能创建Message");
		this.message=frame.getDate();
		this.frame=frame;
	}
	@Override
	public byte[] getMessage() {
		return message;
	}

	@Override
	public void writeMessage(byte[] data) {
		frame.write(data);
	}

	@Override
	public void close() {
		frame.close();
	}

	@Override
	public void flush() {
		frame.flush();
	}
	@Override
	public ITransport getTransport() {
		return frame.getTransport();
	}

	public IFrame getFrame() {
		return frame;
	}
	@Override
	public Object attach() {
		return frame.attach();
	}

}
