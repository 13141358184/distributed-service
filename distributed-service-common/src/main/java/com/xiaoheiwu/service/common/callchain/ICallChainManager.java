package com.xiaoheiwu.service.common.callchain;

public interface ICallChainManager {
	/**
	 * 创建一个调用链。如果调用的方法在调用链中，则返回存在的调用链
	 * @return
	 */
	public CallChain createCallChain();
	
	/**
	 * 
	 * @return 返回调用链信息
	 */
	public CallChain get();
	
	/**
	 * 
	 * @param callChain 设置调用链信息
	 */
	public void set(CallChain callChain);
	
	/**
	 * 移除调用链信息
	 * @param callChain
	 */
	public void remove();
}
