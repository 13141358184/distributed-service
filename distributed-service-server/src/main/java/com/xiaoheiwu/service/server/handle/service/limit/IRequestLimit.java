package com.xiaoheiwu.service.server.handle.service.limit;

import com.xiaoheiwu.service.manager.governance.IRequestAccess;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.server.protocol.IServerRequest;


/**
 * 主要用于限流,當流量到某個上限,会自动拒绝服务
 * @author Chris
 *
 */
public interface  IRequestLimit extends IRequestAccess<String, IServerRequest>{
	/**
	 * @param timeUnit 限流的单位
	 * @param requestCount 单位时间内的请求数
	 */
	public boolean setLimit(TimeUnit timeUnit, int requestCount);
	

	
	public TimeUnit getTimeUnit();
	
	public int getRequestCount();
}
