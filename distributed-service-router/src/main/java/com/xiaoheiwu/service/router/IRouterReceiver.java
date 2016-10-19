package com.xiaoheiwu.service.router;

import com.xiaoheiwu.service.protocol.IServiceResponse;

public interface IRouterReceiver {
	/**
	 * 当调用完成后的回调函数
	 * @param message 返回值的数据
	 * @param attach 传送数据前保存的附件
	 */
	public void receive(IServiceResponse response);
	
	public IServiceResponse getServiceResponse();
}
