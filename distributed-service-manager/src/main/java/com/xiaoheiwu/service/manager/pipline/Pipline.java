package com.xiaoheiwu.service.manager.pipline;

import java.util.LinkedList;
import java.util.List;



public class Pipline<T> implements IPipline<T>{
	protected List<T> requestHandles=new LinkedList<T>();
	
	
	@Override
	public IPipline<T> addHandle(T handle) {
		requestHandles.add(handle);
		return this;
	}
}
