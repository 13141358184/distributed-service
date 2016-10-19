package com.xiaoheiwu.service.client.services;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

import com.xiaoheiwu.service.client.event.CallEvent;
import com.xiaoheiwu.service.client.event.ClientEventType;
import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.protocol.impl.ClientRequest;
import com.xiaoheiwu.service.client.protocol.impl.ClientResponse;
import com.xiaoheiwu.service.client.receiver.CommonReciever;
import com.xiaoheiwu.service.client.receiver.IReceiver;
import com.xiaoheiwu.service.client.receiver.SynchronizationReceiver;
import com.xiaoheiwu.service.client.sender.ISender;
import com.xiaoheiwu.service.client.sender.impl.QueueSender;
import com.xiaoheiwu.service.client.sender.impl.RetrySender;
import com.xiaoheiwu.service.client.sender.impl.TransportSender;
import com.xiaoheiwu.service.common.IdGenerator.IServiceGenerator;
import com.xiaoheiwu.service.common.IdGenerator.ServiceGenerator;
import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.callchain.ICallChainManager;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.transport.ITransport;
/**
 * 服务代理，根据调用服务的接口生成代理
 * @author Chris
 *
 */
public class ServiceProxy implements InvocationHandler {
	private ISender sender;
	private String serviceInterfaceName;//接口名称
	private IReceiver receiver;//调用返回后的回调接口
	private boolean isAsyn;//是否是异步调用，如果是异步调用，方法返回null
	private ITransport transport;
	private static IServiceGenerator serviceGenerator=new ServiceGenerator();
	private ICallChainManager callChainManager=ComponentProvider.getInstance(ICallChainManager.class);
	public ServiceProxy(String serviceInterfaceName,boolean isAsyn,IReceiver receiver, ITransport transport){
		sender=ComponentProvider.getInstance(ISender.class);//发送请求到服务器端
		this.serviceInterfaceName=serviceInterfaceName;
		this.isAsyn=isAsyn;
		if(receiver!=null)this.receiver=new CommonReciever(receiver);
		this.transport=transport;
		if(this.receiver==null){
			createReceiver();
		}
				
	}
	public ServiceProxy(String serviceInterfaceName){
		this(serviceInterfaceName, false, null,null);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		CallChain callChain=callChainManager.createCallChain();
		long serviceCallId=serviceGenerator.getServiceCallId();
		ClientResponse response=createResponse(serviceCallId, method.getName());
		ClientRequest call=new ClientRequest(serviceInterfaceName, method, args ,response, callChain);
		call.setTransport(transport);
		call.setProtocolId((byte)0);//can set by configure
		CallEvent.fireEvent(ClientEventType.CALL_SEND_EVENT, response);
		
		sender.sendServiceCall(call);
		return returnResult(response);
	}

	

	/**
	 * 创建response对象
	 * @return
	 */
	protected ClientResponse createResponse(long serviceCallId, String methodName) {
		ClientResponse response=new ClientResponse(serviceCallId,serviceInterfaceName, methodName);
		response.setReceiver(receiver);
		return response;
	}

	
	/**
	 * 如果是异步调用，直接返回；否则阻塞线程，等待结果返回
	 * @param response
	 * @return
	 */
	protected Object returnResult(ClientResponse response) {
		if(isAsyn){
			return null;
		}
		waitResponse(response);
		if(response.getResponseCode()==ResponseCode.SUCCESS){
			return response.getReturnObject();
		}
		throw new RuntimeException(response.getResponseCode()+":"+response.getDescription());
	}
	

	/**
	 * 如果是同步调用，则阻塞当前线程，知道收到返回值
	 * @param response
	 */
	protected void waitResponse(ClientResponse response) {
		synchronized (response) {
			try {
				response.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 如果没有传入Receiver,且是同步调用，则创建一个同步回调器
	 */
	protected void createReceiver() {
		if(receiver==null&&!isAsyn){
			receiver=new CommonReciever(new SynchronizationReceiver());
		}
	}
	
	
}
