package com.xiaoheiwu.service.common.event;
/**
 * 事件监听器。在响应时间触发的时候，可以被触发
 * @author Chris
 *
 */
public interface IListerner {

	public void execute(IEvent event);
}
