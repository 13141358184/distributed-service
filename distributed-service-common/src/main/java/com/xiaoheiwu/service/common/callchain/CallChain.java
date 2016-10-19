package com.xiaoheiwu.service.common.callchain;

import java.util.concurrent.atomic.AtomicInteger;

import com.xiaoheiwu.service.common.IdGenerator.IServiceGenerator;
import com.xiaoheiwu.service.common.IdGenerator.ServiceGenerator;
import com.xiaoheiwu.service.common.util.IPUtil;

public class CallChain {
	private volatile String chainId;
	private volatile AtomicInteger index=new AtomicInteger(0);
	private volatile boolean inService=false;//是否进入业务方法。在没进入业务方法前，新方法的调用，会启用新的调用链
	private static  IServiceGenerator generator=new ServiceGenerator();
	public CallChain(){
		chainId=IPUtil.getIp()+";"+(generator.getServiceCallId());
		this.index.set(0);
	}
	public String getChainId() {
		return chainId;
	}
	public int getIndex() {
		return index.get();
	}
	public boolean getInService() {
		return inService;
	}
	public boolean isInService() {
		return inService;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public void incrementIndex() {
		this.index.incrementAndGet();
	}
	public void setInService(boolean inService) {
		this.inService = inService;
	}
	
	public void setIndex(int index) {
		this.index.set(index);
	}
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("chainId="+this.chainId+";index="+index+";inService="+inService);
		return sb.toString();
	}
	
}
