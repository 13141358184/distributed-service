package com.xiaoheiwu.service.common.callchain;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;


@Service
public class CallChainManager implements ICallChainManager{
	private static ThreadLocal<CallChain> local=new ThreadLocal<CallChain>();
	
	public CallChain createCallChain(){
		CallChain callChain=local.get();
		if(callChain!=null&&callChain.isInService()){
			callChain.incrementIndex();
			return callChain;
		}
		callChain =new CallChain();
		return callChain;
	}
public static void main( String[] args){
	System.out.print("hello world");
}
	public CallChain get(){
		return local.get();
	}
	
	public void set(CallChain callChain){
		this.local.set(callChain);
	}
	
	public void remove(){
		local.remove();
	}
	
	
	
}
