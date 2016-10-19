package com.xiaoheiwu.service.container;

public interface IContainerConfigure {

	public static final String SERVICE_THREAD_COUNT="SERVICE_THREAD_COUNT";//线程池大小
	
	public static final String SERVICE_THREAD_SERVICE="SERVICE_THREAD_SERVICE";//全局线程池，在服务没有指定线程池或公关服务的情况下使用。
}
