package com.xiaoheiwu.service.common.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.xiaoheiwu.service.common.executor.impl.MutilExecutorService;

public class MutilExecutors {


	
	public static ExecutorService newCachedThreadPool(){
		return new MutilExecutorService(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
	}
	public static ExecutorService newExecutorService(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue){
		return new MutilExecutorService(corePoolSize, maximumPoolSize,
				keepAliveTime, unit,
				workQueue);
		
	}
	public static ExecutorService newFixedThreadPool(int nThreads){
		return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
	}
	public static ExecutorService newFixedThreadPool(int nThreads,ThreadFactory threadFactory){
		return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
	}
	public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory){
		
		return new MutilExecutorService(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),threadFactory);
	}
	
	public static ExecutorService newCachedThreadPool(int corePoolSize,ThreadFactory threadFactory){
		return new MutilExecutorService(corePoolSize, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
	}
	
}
