package com.xiaoheiwu.service.common.executor.impl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.xiaoheiwu.service.common.executor.IMonitorExecutorService;
import com.xiaoheiwu.service.common.executor.IThreadCount;

public class MutilExecutorService implements IMonitorExecutorService{
	private ExecutorService executorService=null;
	private final BlockingQueue<Runnable> workQueue;
	private int maximumPoolSize;
	
	public MutilExecutorService(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler){
		this.workQueue=workQueue;
		this.maximumPoolSize=maximumPoolSize;
		executorService=new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory,handler);
	}
	public MutilExecutorService(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory){
		this.workQueue=workQueue;
		this.maximumPoolSize=maximumPoolSize;
		executorService=new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
	}
	public MutilExecutorService(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue){
		this.workQueue=workQueue;
		this.maximumPoolSize=maximumPoolSize;
		executorService=new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	public MutilExecutorService(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit , BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler){
		this.workQueue=workQueue;
		this.maximumPoolSize=maximumPoolSize;
		executorService=new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,workQueue,handler);
	}
	
	private IThreadCount threadCount=new IThreadCount() {
		private AtomicInteger atomicInteger=new AtomicInteger(0);
		@Override
		public void increment() {
			atomicInteger.incrementAndGet();
		}
		
		@Override
		public void decrement() {
			atomicInteger.decrementAndGet();
		}

		@Override
		public int get() {
			return atomicInteger.get();
		}
	};
	@Override
	public void shutdown() {
		executorService.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return executorService.submit(new MutilCallable<T>(task,threadCount));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return executorService.submit(new MutilRunnable(task,threadCount), result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return executorService.submit(new MutilRunnable(task,threadCount));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {
		return executorService.invokeAll(MutilCallable.convert(tasks,threadCount));
	}

	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.invokeAll(MutilCallable.convert(tasks,threadCount), timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return executorService.invokeAny(MutilCallable.convert(tasks,threadCount));
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return executorService.invokeAny(MutilCallable.convert(tasks,threadCount), timeout, unit);
	}

	@Override
	public void execute(Runnable command) {
		executorService.execute(new MutilRunnable(command,threadCount));
	}

	@Override
	public int getBlockThreadCount() {
		return this.workQueue.size();
	}

	@Override
	public int getUsedThreadCount(){
		return this.threadCount.get();
	}

	public int getMaxThreadCount(){
		return this.maximumPoolSize;
	}
}
