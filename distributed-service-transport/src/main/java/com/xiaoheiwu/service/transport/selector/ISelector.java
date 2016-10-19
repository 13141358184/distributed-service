package com.xiaoheiwu.service.transport.selector;

import java.nio.channels.SelectionKey;

import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.IFrame;
import com.xiaoheiwu.service.transport.handle.IReadHandle;
import com.xiaoheiwu.service.transport.handle.ISelectorHandle;

/**
 * 完成就绪选择
 * @author Chris
 *
 */

public interface ISelector extends Runnable{

	/**
	 * 清理selectionkey
	 * @param key
	 */
	public void cleanupSelectionKey(SelectionKey key);
	
	
	/**
	 * 终止就绪选择过程。退出选择循环，线程结束
	 */
	public void stop();
	
	/**
	 * 如果选择的是accept事件，设置处理此事件的处理器
	 * @param handle
	 */
	public boolean addAcceptSelectorHandle(ISelectorHandle handle);
	
	/**
	 * 如果选择的是read事件，设置处理此事件的处理器
	 * @param handle
	 */
	public boolean addReadSelectorHandle(ISelectorHandle handle);
	
	
	/**
	 * 如果选择的是write事件，设置处理此事件的处理器
	 * @param handle
	 */
	public boolean addWriteSelectorHandle(ISelectorHandle handle);
	
	
	public void register(ITransport transport, int opts, IFrame frame);

	
	/**
	 * 给这个selector注册一个兴趣事件
	 * @param channel
	 * @param opts
	 * @return
	 */
	public void register(ITransport channel, int opts, Object attach);
	

	/**
	 * 给这个selector注册一个兴趣事件
	 * @param channel
	 * @param opts
	 * @return
	 */
	public void register(ITransport channel, int opts);
	

	
	/**
	 * 如果其他线程调用此方法，select方法会立刻返回。解除阻塞
	 */
	public void wakeup();
	
	/**
	 * 开始就绪选择，成功返回true，失败返回false
	 */
	public boolean select();
	
	
	public void addReadHandle(IReadHandle readHandle);

	
}
