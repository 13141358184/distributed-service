package com.xiaoheiwu.service.server.handle;

import com.xiaoheiwu.service.protocol.ResponseCode;

public interface IRequestAccess <T> {
	/**
	 * 用来设置配置信息值
	 * @param t
	 */
	public boolean setValue(T t);
	
	
	/**
	 * 申请访问，如果放回false，则拒绝访问。否则增加访问数。
	 * @return true可以访问，false，不允许访问
	 */
	public boolean access();
	
	/**
	 * 
	 * @return 获取配置参数
	 */
	public T getValue();
	
	/**
	 * 
	 * @return 如果发生了限制，应该抛出的异常 
	 */
	public ResponseCode getExceptionResponseCode();
	
}
