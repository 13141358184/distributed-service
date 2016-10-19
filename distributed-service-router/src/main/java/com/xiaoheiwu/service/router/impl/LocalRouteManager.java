package com.xiaoheiwu.service.router.impl;

import java.util.List;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.transport.ITransport;
public class LocalRouteManager extends SenderAndReceiveRouterManager{
	private static final  LocalRouteManager instance=new LocalRouteManager();
	private LocalRouteManager(){
		
	}
	public static IRouterManager getInstance(){
		return instance;
	}
	@Override
	public ITransport getTransport(IServiceRequest request) {
		return factory.getLocalTransport(getPort(request));
	}

	private int getPort(IServiceRequest request) {
		List<IServiceNode> nodes=getServiceNodes(request);
		if(nodes==null||nodes.size()==0)throw new RuntimeException("不能获取端口信息。"+request.toString());
		return nodes.iterator().next().getPort();
	}

}
