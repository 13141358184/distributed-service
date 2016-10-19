package com.xiaoheiwu.service.client.sender.impl;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.sender.ISender;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.component.ComponentProvider;

//代发送的请求放在一个队列中，然后通过自身的轮询来完成最终的发送,此发送器只保证数据被发送出去，至于最终能不能成功依靠后续的组建完成
@Service
@ServiceDefault(ISender.class)
public class QueueSender implements ISender,Runnable{
	protected Logger logger=Logger.getLogger(this.getClass());
	
	private ConcurrentLinkedQueue<IClientRequest> sendQueue=new ConcurrentLinkedQueue<IClientRequest>();
	private volatile boolean isStart=false;//发送线程是否启动
	private ISender sender=ComponentProvider.getInstance(RetrySender.class);
	@Override
	public boolean sendServiceCall(IClientRequest request) {
		if(request==null)return false;
		sendQueue.offer(request);
		notifySendQueue();
		return true;
	}
	
	

	@Override
	public void run() {
		while(true){
			IClientRequest serviceCall=sendQueue.poll();
			if(serviceCall==null){
				waitQueue();
			}
			if(serviceCall!=null)send(serviceCall);
		}
		
		
	}
	protected void send(IClientRequest serviceCall) {
		sender.sendServiceCall(serviceCall);
	}
	/**
	 * 当有数据到来时，激活发送线程
	 */
	private void notifySendQueue() {
		startSendThread();
		synchronized (sendQueue) {
			sendQueue.notify();
		}
	}
	/**
	 * 如果线程没有启动，则启动发送线程
	 */
	private void startSendThread() {
		if(!isStart){
			synchronized (this) {
				if(!isStart){
					Thread thread=new Thread(this);
					thread.start();
					isStart=true;
				}
			}
		}
	}
	/**
	 * 如果队列为空，则等待，知道队列有数据
	 */
	private void waitQueue() {
		synchronized (sendQueue) {
			try {
				sendQueue.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}
