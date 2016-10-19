package com.xiaoheiwu.service.transport.handle;

import com.xiaoheiwu.service.transport.message.IMessage;


public interface IReadHandle extends IHandle{

	/**
	 * 
	 * @param message 处理message
	 * @return true，继续执行，false停止后续执行
	 */
	public boolean handle(IMessage message);
	
}
