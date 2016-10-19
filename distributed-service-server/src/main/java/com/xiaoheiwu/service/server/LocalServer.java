package com.xiaoheiwu.service.server;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.router.ILocalServer;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.impl.SelectorRouterManager;
import com.xiaoheiwu.service.server.handle.IPiplineRequestHandle;
import com.xiaoheiwu.service.server.protocol.IServerRequest;
import com.xiaoheiwu.service.server.protocol.impl.ServerRequest;
import com.xiaoheiwu.service.server.service.IServiceRegistion;

import com.xiaoheiwu.service.transport.ITransport;

public class LocalServer implements ILocalServer{
	private Log logger=Log.getLogger(this.getClass());
	@Autowired
	private IServiceRegistion serviceRegistion;
	private IRouterManager routerManager=ComponentProvider.getInstance(SelectorRouterManager.class);
	private IPiplineRequestHandle piplineRequestHandle;
	private int port;

	public LocalServer(int port,IPiplineRequestHandle piplineRequestHandle){
		this.piplineRequestHandle=piplineRequestHandle;
		this.port=port;
	}
	@Override
	public boolean containService(String serviceName) {
		return serviceRegistion.getInvoker(serviceName)!=null;
	}

	@Override
	public void invokeCall(IServiceRequest call, ITransport transport) {
		logger.debug("获取到一个系统调用消息");
		IServerRequest request=new ServerRequest(call, transport,routerManager);
		piplineRequestHandle.handle(request);
		logger.debug("完成一个系统调用消息，调用的服务为："+call.getServiceName()+":"+call.getMethodName());
	}

	@Override
	public int getPort() {
		return port;
	}

}
