package com.xiaoheiwu.service.common.executor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.callchain.ICallChainManager;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.executor.IThreadCount;

public class MutilCallable<T> implements Callable<T>{
	private Callable<T> object;
	private IThreadCount count;
	private CallChain callChain;
	private ICallChainManager chainManager=ComponentProvider.getInstance(ICallChainManager.class);
	public MutilCallable(Callable<T> object, IThreadCount count){
		this.object=object;
		this.count=count;
		callChain=chainManager.get();
	}
	@Override
	public T call() throws Exception {
		chainManager.set(callChain);
		count.increment();
		T t=object.call();
		count.decrement();
		return t;
	}
	
	public static <T> Collection<? extends Callable<T>> convert(Collection<? extends Callable<T>> tasks,IThreadCount count){
		List<Callable<T>> collections=new ArrayList<Callable<T>>();
		for(Callable<T> callable:tasks){
			MutilCallable<T> task=new MutilCallable<T>(callable,count);
			collections.add(task);
		}
		return collections;
	} 

}
