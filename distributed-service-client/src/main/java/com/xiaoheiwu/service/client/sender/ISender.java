package com.xiaoheiwu.service.client.sender;

import com.xiaoheiwu.service.client.protocol.IClientRequest;

public interface ISender {
	public static final String SENDER_TIMEOUT="sender.timeout";
	public static final String SENDER_RETRY_COUNT="sender.retry.count";

	/**
	 * 把服务调用发送到服务器端
	 * @param serviceCall
	 * @return
	 */
	public boolean sendServiceCall(IClientRequest request);
	
}
