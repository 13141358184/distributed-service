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

public class WriteSelectorHandle implements ISelectorHandle{
	protected List<IReadHandle> writeHandles=new ArrayList<IReadHandle>();

	public WriteSelectorHandle(){
	}

	@Override
	public int getHandleType() {
		return ISelectorHandle.WRITE;
	}

	@Override
	public void handle(SelectionKey key) {
		IFrame frame=(IFrame) key.attachment();
		if(frame==null){
			key.cancel();
			System.out.println("没有写的Frame");
			return ;
		}
	
		frame.flush();
		key.cancel();
	}
	
	@Override
	public boolean addHandle(IHandle handle) {
		if(handle.getHandleType()!=IHandle.WRITE)return false;
		if(!IReadHandle.class.isInstance(handle))return false;
		synchronized (writeHandles) {
			writeHandles.add((IReadHandle)handle);
		}
		return true;
	}
}
