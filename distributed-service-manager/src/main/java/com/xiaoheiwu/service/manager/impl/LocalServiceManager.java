package com.xiaoheiwu.service.manager.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sun.swing.internal.plaf.synth.resources.synth;
import com.xiaoheiwu.service.annotation.util.ServiceUtils;
import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceManager;

@Service
public class LocalServiceManager implements IServiceManager{
	private Log logger=Log.getLogger(this.getClass());
	public static final String PROVIDER_NAME="com.xiaoheiwu.service.manager.LocalServiceManagerProvider";
	private ConcurrentMap<String, List<IServiceNode>> service2Node=new ConcurrentHashMap<String, List<IServiceNode>>();
	private ConcurrentMap<String, IService> services=new ConcurrentHashMap<String, IService>();
	@Override
	public IService getService(String name) {
		return services.get(name);
	}
	
	@Override
	public synchronized IServiceNode publishServiceNode(IService service, IServiceNode serverNode) {
		return publishServiceNode(service, serverNode, true);
	}
	@Override
	public synchronized IServiceNode publishServiceNode(IService service, IServiceNode serverNode, boolean enable) {
		if(!enable)return serverNode;
		String name=service.getName();
		services.put(name, service);
		List<IServiceNode> nodes=service2Node.get(name);
		if(nodes==null){
			nodes=new ArrayList<IServiceNode>();
			service2Node.put(name, nodes);
		}
		nodes.add(serverNode);
		return serverNode;
	}
	public void rePublishAllServiceNode(String name, List<IServiceNode> nodes){
		service2Node.put(name, nodes);
	}
	@Override
	public List<IServiceNode> getSeviceNodes(String name){
		return this.getSeviceNodes(name, false);
	}
	@Override
	public List<IServiceNode> getSeviceNodes(String name, boolean listern) {
		return service2Node.get(name);
	}

	@Override
	public synchronized boolean disableServiceNode(String name, IServiceNode node) {
		IService service=getService(name);
		if(service==null){
			logger.error("不能激活node，因为service缺失");
			return false;
		}
		synchronized (service2Node) {
			List<IServiceNode> nodes=service2Node.get(name);
			Iterator<IServiceNode> it = nodes.iterator();
			IServiceNode serverNode=null;
			while(it.hasNext()){
				serverNode=it.next();
				if(serverNode.equals(node)){
					it.remove();
					break;
				}
			}
		}
		return true;
	}

	@Override
	public synchronized boolean enableServiceNode(String name, IServiceNode node) {
		IService service=getService(name);
		if(service==null){
			logger.error("不能激活node，因为service缺失");
			return false;
		}
		publishServiceNode(service, node);
		return true;
	}
	@Override
	public void deleteService(String name) {
		this.services.remove(name);
		this.service2Node.remove(name);
	}
	@Override
	public IServiceNode getSeviceNode(String serviceName, String identity,
			boolean enable) {
		if(!enable)return null;
		List<IServiceNode> nodes=getSeviceNodes(serviceName);
		if(nodes==null)return null;
		for(IServiceNode node:nodes){
			if(node.getIdentity().equals(identity))return node;
		}
		return null;
	}
	@Override
	public IServiceNode getSeviceNode(String serviceName, String identity) {
		return getSeviceNode(serviceName, identity, true);
	}
	@Override
	public void listernService(IService service) {
		throw new RuntimeException("不支持这个方法");
	}
	@Override
	public void listernServiceNode(IServiceNode serviceNode) {
		throw new RuntimeException("不支持这个方法");
	}
	
	
	
	
}
