package com.xiaoheiwu.service.client.sender.impl;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.client.protocol.impl.ClientRequest;
import com.xiaoheiwu.service.client.sender.ISender;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.manager.context.ApplicationContext;
import com.xiaoheiwu.service.protocol.ResponseCode;
/**
 * 主要完成发送的可靠性，在发送成功前将发送信息暂存，如果固定时间没有完成，则重发，超过重发次数，返回异常
 * @author Chris
 *
 */
@Service
public class RetrySender implements ISender, Runnable {
	protected ConcurrentMap<Long, IClientRequest> sendingQueue = new ConcurrentHashMap<Long, IClientRequest>();
	protected boolean isstart=false;
	protected ISender sender=ComponentProvider.getInstance(TransportSender.class);
	protected ApplicationContext applicationContext=ApplicationContext.getInstance();
	private int timeout=applicationContext.getIntValue(ISender.SENDER_TIMEOUT);
	private int sendRetryCount=applicationContext.getIntValue(ISender.SENDER_TIMEOUT);
	@Override
	public boolean sendServiceCall(IClientRequest serviceCall) {
		boolean fail = retryCountCheck(serviceCall);
		if (fail) {
			return false;
		}
		addSendingQueue(serviceCall);
		send(serviceCall);
		return true;
	}

	
	protected void send(IClientRequest serviceCall) {
		sender.sendServiceCall(serviceCall);
	}

	/**
	 * 轮询已经发送的调用，如果超时则重发，超时超过一定次数，调用失败返回应用层
	 */
	@Override
	public void run() {
		while(true){
			if(this.sendingQueue.isEmpty()){
				synchronized (this.sendingQueue) {
					try {
						this.sendingQueue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			Iterator<IClientRequest> it = this.sendingQueue.values().iterator();
			while(it.hasNext()){
				IClientRequest serviceCall=it.next();
				
				boolean finished=serviceCall.getResponse().isFinished();
				if(finished){
					this.sendingQueue.remove(serviceCall.getServiceCallId());
					continue;
				}
				if(System.currentTimeMillis()-serviceCall.getSendTime()>timeout){
					this.sendingQueue.remove(serviceCall.getServiceCallId());
					this.sendServiceCall(serviceCall);
				}
			}
		}
	}
	/**
	 * 检查发送是否超时，如果超时，则直接返回错误
	 * @param serviceCall
	 * @return
	 */
	private boolean retryCountCheck(IClientRequest serviceCall) {
		if (serviceCall.getRetryCount() > sendRetryCount) {
			IClientResponse response=serviceCall.getResponse();
			response.finishedResponse(ResponseCode.TIMEOUT_EXCEPTION, ResponseCode.TIMEOUT_EXCEPTION.name(), null);
			return true;
		}
		return false;
	}
	protected void addSendingQueue(IClientRequest serviceCall) {
		startTimeoutCheck();
		serviceCall.setSendTime(System.currentTimeMillis());
		serviceCall.incRetryCount();
		sendingQueue.put(serviceCall.getServiceCallId(), serviceCall);
		synchronized (this.sendingQueue) {
			sendingQueue.notify();
		}
	}


	private void startTimeoutCheck() {
		if(isstart)return;
		synchronized (this) {
			if(isstart)return;
			Thread thread=new Thread(this);
			thread.start();
			isstart=true;
		}
		
	}



}
