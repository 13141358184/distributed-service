package com.xiaoheiwu.service.common.executor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.callchain.ICallChainManager;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.executor.IThreadCount;

public class MutilRunnable implements Runnable{
	private Runnable object;
	private IThreadCount count;
	private CallChain callChain;
	private ICallChainManager chainManager=ComponentProvider.getInstance(ICallChainManager.class);
	public MutilRunnable(Runnable object, IThreadCount count){
		this.object=object;
		this.count=count;
		callChain=chainManager.get();
	}
	public MutilRunnable(Runnable object){
		this(object, null);
	}
	@Override
	public void run() {
		chainManager.set(callChain);
		if(count!=null)count.increment();
		object.run();
		if(count!=null)count.decrement();
	}
	public static <T> Collection<? extends Runnable> convert(Collection<? extends Runnable> tasks,IThreadCount count){
		List<Runnable> collections=new ArrayList<Runnable>();
		for(Runnable runnable:tasks){
			Runnable task=new MutilRunnable(runnable, count);
			collections.add(task);
		}
		return collections;
	} 
	public static Runnable getMutileRunnable(Runnable runnable){
		return new MutilRunnable(runnable);
	}
}
