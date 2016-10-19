package com.xiaoheiwu.service.server.handle;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.server.invoker.IInvoker;
import com.xiaoheiwu.service.server.protocol.IServerRequest;

/**
 * 队列接口
 * @author yuan_
 *
 */

public interface IInvokerQueue {
	/**
	 * 把一个reques插入队列，如果队列已经满了，则阻塞
	 * @param request
	 */
	public void putInvoker(IInvoker invoker);
	
	/**
	 * 
	 * @return 返回队列中的数据，如果队列为空则阻塞
	 */
	public IInvoker takeInvoker();
}
