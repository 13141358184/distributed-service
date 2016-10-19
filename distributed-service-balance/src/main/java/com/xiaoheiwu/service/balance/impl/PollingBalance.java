package com.xiaoheiwu.service.balance.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.xiaoheiwu.service.balance.BalanceType;
import com.xiaoheiwu.service.balance.IBalance;
import com.xiaoheiwu.service.balance.IAccessFilter;

public class PollingBalance<T> implements IBalance<T>{
	private AtomicInteger counter=new AtomicInteger(0);
	private List<T> elements;
	private IAccessFilter<T> processor;
	protected PollingBalance(){
		
	}
	public PollingBalance(List<T> elements){
		this.elements=elements;
	}
	public PollingBalance(List<T> elements, IAccessFilter<T> processor){
		this.elements=elements;
		this.processor=processor;
	}
	@Override
	public <D> T doBalance() {
		return doBalance(processor);
	}
	@Override
	public  <D>  T doBalance(IAccessFilter<T> processor) {
		int count=getCount();
		int length=elements.size();
		int step=1;
		while(step<=length){
			T t=elements.get(count%length);
			boolean enable=enable(t,processor);
			if(enable)return t;
			count++;
			counter.incrementAndGet();
			step++;
		}
		return null;
		
	}

	private boolean enable(T t,IAccessFilter<T> processor2) {
		if(processor2!=null&&!processor2.enable(t))return false;
		if(this.processor!=null&&this.processor!=processor2&&!this.processor.enable(t))return false;
		return true;
	}
	@Override
	public void setProcessor(IAccessFilter<T> processor) {
		this.processor=processor;
	}
	@Override
	public void setElement(List<T> elements) {
		this.elements=elements;
	}
	@Override
	public void addElement(T element) {
		if(this.elements.contains(element))return;
		this.elements.add(element);
	}
	@Override
	public void removeElement(T element) {
		this.elements.remove(element);
	}
	
	private int getCount() {
		int count=counter.get();
		if(count>=(Integer.MAX_VALUE-100000)){
			synchronized (this) {
				if(count>=(Integer.MAX_VALUE-100000)){
					counter.set(0);
				}
			}
		}
		return counter.incrementAndGet();
	}
	@Override
	public void synchronize(IBalance<T> b) {
		if(!this.getClass().isInstance(b)){
			return;
		}
		PollingBalance<T> pb=(PollingBalance<T>)b;
		this.counter=pb.counter;
	}
	@Override
	public BalanceType getType() {
		return BalanceType.POLLING;
	}
	@Override
	public List<T> getElements() {
		return this.elements;
	}
}
