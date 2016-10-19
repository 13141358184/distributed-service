package com.xiaoheiwu.service.common.event;

public interface IEventSupport {
	public static final String CLOSE_EVENT="CLOSE_EVENT";
	/**
	 * 
	 * @return 事件对应的编号
	 */
	public String getEvent();
	
	/**
	 * 可以通过这个接口关闭事件的触发
	 * @param support
	 */
	public void supportEvent(boolean support);
	

}
