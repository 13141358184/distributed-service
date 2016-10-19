package com.xiaoheiwu.service.server.handle;

import com.xiaoheiwu.service.server.protocol.IServerRequest;

public interface IRequestHandle {

	/**
	 * 处理一个请求
	 * @param request 请求
	 * @return 如果需要立刻返回，则返回false，否则返回true。返回true，下面的handle会继续执行
	 */
	public boolean handle(IServerRequest request);
	
	

}
