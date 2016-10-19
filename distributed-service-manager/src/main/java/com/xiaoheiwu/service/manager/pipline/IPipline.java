package com.xiaoheiwu.service.manager.pipline;



public interface IPipline<T> {
	/**
	 * 
	 * @param handle 执行handle
	 * @return this。为了是实现级联关系
	 */
	public IPipline<T> addHandle(T handle);
}
