package com.xiaoheiwu.service.common.event;

/**
 * 事件
 * @author Chris
 *
 */
public interface IEvent {
	
	/**
	 * 
	 * @return 时间类型
	 */
	public String getEvent();
	
	/**
	 * 触发事件
	 */
	public void fireEvent();
}
