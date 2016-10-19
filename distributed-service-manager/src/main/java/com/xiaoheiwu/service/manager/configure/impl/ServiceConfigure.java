package com.xiaoheiwu.service.manager.configure.impl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.IServiceConfigure;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.event.ConfigureChangeEvent;
import com.xiaoheiwu.service.manager.event.NodeChangeEvent;
import com.xiaoheiwu.service.manager.event.ServiceChangeEvent;
@Service
@ServiceDefault(IServiceConfigure.class)
public class ServiceConfigure implements IServiceConfigure,IListerner{
	private Log log=Log.getLogger(this.getClass());
	private volatile ConcurrentMap<String, IService> serviceConfigure=new ConcurrentHashMap<String, IService>();
	private volatile ConcurrentMap<String, Map<String, IServiceNode>> serviceNodeConfigure=new ConcurrentHashMap<String, Map<String, IServiceNode>>();
	public ServiceConfigure(){
		NodeChangeEvent.addListerner(NodeChangeEvent.NODE_CHANGE_EVENT, this);
		ServiceChangeEvent.addListerner(ServiceChangeEvent.SERVICE_CHANGE_EVENT, this);
	}
	@Override
	public IService getService(String serviceName) {
		IService service=serviceConfigure.get(serviceName);
		
		return service;
	}

	@Override
	public IServiceNode getServiceNode(String serviceName, String identity) {
		String result=null;
		Map<String, IServiceNode> nodes=serviceNodeConfigure.get(serviceName);
		if(nodes!=null){
			IServiceNode node=nodes.get(identity);
			return node;
		}
		return null;
	}

	@Override
	public void execute(IEvent event) {
		log.debug("SERVICE_CONFIGURE_EVENT",event.toString());
		if(NodeChangeEvent.class.isInstance(event)){
			NodeChangeEvent changeEvent=(NodeChangeEvent)event;
			String serviceName=changeEvent.getServiceName();
			IServiceNode serviceNode=changeEvent.getServerNode();
			IService service=serviceNode.getService();
			this.serviceConfigure.put(serviceName, service);
			Map<String,IServiceNode> nodes=this.serviceNodeConfigure.get(serviceName);
			if(nodes==null){
				nodes=new ConcurrentHashMap<String, IServiceNode>();
				serviceNodeConfigure.put(serviceName, nodes);
			}
			nodes.put(serviceNode.getIdentity(),serviceNode);
			new ConfigureChangeEvent(this, serviceName, serviceNode.getIdentity()).fireEvent();
		}else if(ServiceChangeEvent.class.isInstance(event)){
			ServiceChangeEvent serviceChangeEvent=(ServiceChangeEvent)event;
			String serviceName=serviceChangeEvent.getName();
			IService service=serviceChangeEvent.getService();
			this.serviceConfigure.put(serviceName, service);
			new ConfigureChangeEvent(this, serviceName,null).fireEvent();
		}
		
		
	}

}
