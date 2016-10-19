package com.xiaoheiwu.service.common.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

import com.xiaoheiwu.service.common.annotation.Componet;

public class ServiceGenerator implements IServiceGenerator{
	protected AtomicLong servieGenerator=new AtomicLong(0);
	@Override
	public long getServiceCallId() {
		return generateServiceCallId();
	}
	private long generateServiceCallId() {
		if(this.servieGenerator.get()>Long.MAX_VALUE-1000000){
			synchronized (servieGenerator) {
				if(this.servieGenerator.get()>Long.MAX_VALUE-1000000){
					this.servieGenerator.set(0);
				}
			}
		}
		return servieGenerator.incrementAndGet();
	}
}
