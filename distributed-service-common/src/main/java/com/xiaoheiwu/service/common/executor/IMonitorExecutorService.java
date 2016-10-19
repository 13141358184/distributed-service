package com.xiaoheiwu.service.common.executor;

import java.util.concurrent.ExecutorService;

public interface IMonitorExecutorService extends ExecutorService{

	/**
	 * 
	 * @return 返回队列中的线程数，如果为0说明还可以有线程可用
	 */
	public int getBlockThreadCount();
	
	/**
	 * 
	 * @return 返回已经使用线程的个数
	 */
	public int getUsedThreadCount();
	
	
	/***
	 * 
	 * @return 返回可创建的最大线程个数
	 */
	public int getMaxThreadCount();
	
	
	
	
	
}
