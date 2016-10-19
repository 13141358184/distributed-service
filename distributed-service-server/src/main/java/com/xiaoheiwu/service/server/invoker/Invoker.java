package com.xiaoheiwu.service.server.invoker;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.callchain.ICallChainManager;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.component.Components;
import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.common.executor.MutilExecutors;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.common.log.LogLable;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.server.protocol.IServerRequest;

public class Invoker implements IInvoker {
	private ISerializer serializer=ComponentProvider.getInstance(ISerializer.class);
	private ICallChainManager callChainManager=Components.getInstance().getBean(ICallChainManager.class);
	private Log logger=Log.getLogger(this.getClass());
	private Object serviceImpl;
	private ExecutorService executorService;
	private IConfigure configure;
	public Invoker(Object serviceImpl,ExecutorService executorService, IConfigure configure){
		this.serviceImpl=serviceImpl;
		this.executorService=executorService;
		if(this.executorService==null)this.executorService=MutilExecutors.newFixedThreadPool(1);
		this.configure=configure;
	}
	
	@Override
	public void invoke(final IServerRequest request) {
		getExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				logger.debug("开始一个系统调用，调用的方法是："+request.getServiceName()+":"+request.getMethodName());
				Object object=invoke(request, request.getMethodName(), request.getParameters());
				request.sendSuccessResponse(object);
				logger.debug("完成一个系统调用，调用的结果是："+object.toString());
			}
		});
	}

	@Override
	public Object getServiceImpl() {
		return serviceImpl;
	}

	@Override
	public ExecutorService getExecutor() {
		return executorService;
	}
	private CallChain setAndGetCallChain(IServerRequest request) {
		CallChain callChain=request.getCallChain();
		if(callChain!=null){
			callChain.setInService(true);
			callChainManager.set(callChain);
			return callChain;
		}
		return null;
	}
	private Object invoke(IServerRequest request,  String methodName, Object[] parameters) {
		try{
	
			Class[] classes=new Class[parameters.length];
			int i=0;
			for(Object object: parameters){
				Class clazz=object.getClass();
				classes[i]=serializer.getDataTypeClass(clazz);
				i++;
			}
			CallChain callChain=setAndGetCallChain(request);
			String chainId=null;
			int index=0;
			if(callChain!=null){
				chainId=callChain.getChainId();
				index=callChain.getIndex();
			}
			logger.info(LogLable.PERFORMANCE_INVOKE_BEFORE_EXECUTE,request.getServiceName(),methodName,request.getServiceCallId(),request.getServerIp(),request.getServerPort(),request.getClientIp(),chainId,index,System.currentTimeMillis());
			Method method=getServiceImpl().getClass().getMethod(methodName,classes);
			
		

			Object object= method.invoke(getServiceImpl(), parameters);
			logger.info(LogLable.PERFORMANCE_INVOKE_AFTER_EXECUTE,request.getServiceName(),methodName,request.getServiceCallId(),request.getServerIp(),request.getServerPort(),request.getClientIp(),chainId,index,System.currentTimeMillis());
			
			callChainManager.remove();
			
			return object;
		}catch(Exception e){
			e.printStackTrace();
			logger.info(LogLable.ERROR_SERVICE,request.getServiceName(),methodName,request.getServiceCallId(),e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
