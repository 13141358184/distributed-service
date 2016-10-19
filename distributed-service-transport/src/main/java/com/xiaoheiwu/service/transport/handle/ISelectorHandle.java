package com.xiaoheiwu.service.transport.handle;

import java.nio.channels.SelectionKey;


public interface ISelectorHandle extends IHandle{
	
	
	public void handle(SelectionKey key);
	
	public boolean addHandle(IHandle handle);
	
}
