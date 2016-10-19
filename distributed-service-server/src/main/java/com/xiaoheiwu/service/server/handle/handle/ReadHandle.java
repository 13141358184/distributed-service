package com.xiaoheiwu.service.server.handle.handle;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.protocol.IProtocolService;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.impl.SelectorRouterManager;
import com.xiaoheiwu.service.server.handle.IPiplineRequestHandle;
import com.xiaoheiwu.service.server.protocol.IServerRequest;
import com.xiaoheiwu.service.server.protocol.impl.ServerRequest;
import com.xiaoheiwu.service.transport.handle.IReadHandle;
import com.xiaoheiwu.service.transport.message.IMessage;

public class ReadHandle implements IReadHandle{
	private Log logger=Log.getLogger(this.getClass());
	private IProtocolService protocolService=ComponentProvider.getInstance(IProtocolService.class);
	private IRouterManager routerManager=ComponentProvider.getInstance(SelectorRouterManager.class);
	private IPiplineRequestHandle piplineRequestHandle;
	@Override
	public int getHandleType() {
		return READ;
	}

	@Override
	public boolean handle(IMessage message) {
		logger.debug("获取到一个系统调用消息");
		IServiceRequest call=protocolService.doCall(message.getMessage());
		IServerRequest request=new ServerRequest(call, message.getTransport(),routerManager);
		piplineRequestHandle.handle(request);
		logger.debug("完成一个系统调用消息，调用的服务为："+call.getServiceName()+":"+call.getMethodName());
		return true;
	}

	public void setPiplineRequestHandle(IPiplineRequestHandle piplineRequestHandle) {
		this.piplineRequestHandle = piplineRequestHandle;
	}



}
