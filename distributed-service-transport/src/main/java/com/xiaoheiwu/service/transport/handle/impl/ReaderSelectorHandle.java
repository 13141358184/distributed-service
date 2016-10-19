package com.xiaoheiwu.service.transport.handle.impl;

import java.nio.channels.SelectionKey;


import java.util.ArrayList;
import java.util.List;

import com.xiaoheiwu.service.transport.frame.IFrame;
import com.xiaoheiwu.service.transport.handle.IHandle;
import com.xiaoheiwu.service.transport.handle.IReadHandle;
import com.xiaoheiwu.service.transport.handle.ISelectorHandle;
import com.xiaoheiwu.service.transport.message.IMessage;
import com.xiaoheiwu.service.transport.message.Message;

public class ReaderSelectorHandle implements ISelectorHandle {
	protected List<IReadHandle> readHandles=new ArrayList<IReadHandle>();
	

	@Override
	public void handle(SelectionKey key) {
		IFrame frame = (IFrame) key.attachment();

		try{
			if(!frame.getTransport().isOpen()){
				frame.close();
				return;
			}
			if (frame == null) {
				key.cancel();
				System.out.println("没有读的Frame");
				return;
			}
			int dataCount = frame.read();
//			if(dataCount==0){
////				key.cancel();
//				System.out.println("sb");
//				return;
//			}
			boolean finish = frame.readFinish();
			if (!finish) {
				if (dataCount <= 0){
					key.cancel();
					key.selector().wakeup();
				}
				return;
			}
			key.cancel();
			handle(frame);
		}catch(Exception e){
			e.printStackTrace();
			frame.close();
		}
		
	}

	protected boolean handle(IFrame frame) {
		IMessage message=new Message(frame);
		synchronized (this.readHandles) {
			for(IReadHandle handle: readHandles){
				boolean continueHandle=handle.handle(message);
				if(!continueHandle)return false;
			}
		}
		return true;
	}

	@Override
	public int getHandleType() {
		return ISelectorHandle.READ;
	}

	@Override
	public boolean addHandle(IHandle handle) {
		if(handle.getHandleType()!=IHandle.READ)return false;
		if(!IReadHandle.class.isInstance(handle))return false;
		synchronized (readHandles) {
			readHandles.add((IReadHandle)handle);
		}
		return true;
	}

}
