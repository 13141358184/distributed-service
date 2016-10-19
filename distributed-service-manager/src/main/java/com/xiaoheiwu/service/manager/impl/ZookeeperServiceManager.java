package com.xiaoheiwu.service.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;


import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceManager;
import com.xiaoheiwu.service.zookeeper.ZKClient;
import com.xiaoheiwu.service.zookeeper.util.PathUtil;
@org.springframework.stereotype.Service
public class ZookeeperServiceManager implements IServiceManager{
	private Log logger=Log.getLogger(this.getClass());
	public static final String PROVIDER_NAME="com.xiaoheiwu.service.manager.ZookeeperServiceManagerProvider";
	

	@Override
	public IServiceNode getSeviceNode(String serviceName,String identity) {
		return getSeviceNode(serviceName, identity, true);
	}
	@Override
	public IServiceNode getSeviceNode(String serviceName,String identity,boolean enable) {
		return getSeviceNode(serviceName, identity, enable,false);
	}
	private IServiceNode getSeviceNode(String serviceName,String identity,boolean enable, boolean useWatch) {
		ServiceNode serviceNode=new ServiceNode(identity);
		IService service=getService(serviceName);
		serviceNode.setService(service);
		String serviceNodePath=Service.getServiceNodePath(serviceName, identity, enable);
		Map<String, String > strategy=getNodeValue(serviceNodePath, useWatch);
		serviceNode.setStrategy(strategy);
		return serviceNode;
	}
	@Override
	public List<IServiceNode> getSeviceNodes(String serviceName){
		return getSeviceNodes(serviceName, false);
	}
	@Override
	public List<IServiceNode> getSeviceNodes(String serviceName,boolean listern) {
		String path=Service.getEnablePath(serviceName);
		
		try {
			List<String> children=getChildren(path);
			IService service=getService(serviceName);
			List<IServiceNode> configures=getServiceNodes(service,path,children,listern);
			return configures;
		} catch (Exception e) {
			logger.error("获取节点列表错误，服务name="+serviceName, e);
			e.printStackTrace();
		}
		return new ArrayList<IServiceNode>();
	}
	@Override
	public void rePublishAllServiceNode(String name, List<IServiceNode> nodes){
		String path=Service.getEnablePath(name);
		List<String> children=getChildren(path);
		for(String nodePath: children){
			deleteNode(nodePath);
		}
		IService service=getService(name);
		for(IServiceNode node:nodes){
			this.publishNode(service, node, true);
		}
	}

	@Override
	public IService getService(String name) {
		return getService(name, true);
	}
	@Override
	public IServiceNode publishServiceNode(IService service, IServiceNode node) {
		return publishServiceNode(service, node, true);
	}
	@Override
	public IServiceNode publishServiceNode(IService service, IServiceNode node, boolean enable) {
		publishService(service);
		publishNode(service,node, enable);
		return node;
	}

	@Override
	public boolean disableServiceNode(String serviceName, IServiceNode node) {
		String serviceEnableNodePath=Service.getServiceNodePath(serviceName, node.getIdentity(),true);
		IServiceNode serviceNode=getSeviceNode(serviceName, node.getIdentity());
		if(exist(serviceEnableNodePath,false)){
			deleteNode(serviceEnableNodePath);
		}
		String disablePath=Service.getServiceNodePath(serviceName, node.getIdentity(),false);
		if(!exist(disablePath,false)){
			createNode(disablePath, ZKClient.EPHEMERAL, serviceNode.getStrategy());
		}
		return true;
	}

	@Override
	public boolean enableServiceNode(String serviceName, IServiceNode node) {
		String disablePath=Service.getServiceNodePath(serviceName, node.getIdentity(),false);
		IServiceNode serviceNode=getSeviceNode(serviceName, node.getIdentity(),false);
		if(exist(disablePath,false)){
			deleteNode(disablePath);
		}
		String enablePath=Service.getServiceNodePath(serviceName, node.getIdentity(),true);
		if(!exist(enablePath,false)){
			createNode(enablePath, ZKClient.EPHEMERAL, serviceNode.getStrategy());
		}
		return true;
	}
	


	protected String publishService(final IService service) {
		String name=service.getName();
		String servicePath=Service.getServicePath(name);
		createNode(servicePath, ZKClient.PERSISTENT, service.getStrategy());
		return servicePath;
	}
	private void publishNode(final IService service, IServiceNode node, boolean enable) {
		String name=service.getName();
		String servicePath=Service.getEnablePath(name);
		if(!enable){
			servicePath=Service.getDisablePath(name);
		}
		String nodePath=PathUtil.getServiceSubNodePath(servicePath, node.getIdentity());
		createNode(nodePath, ZKClient.EPHEMERAL, node.getStrategy());
	}
	private <T> void createNode(String path, CreateMode mode, T t){
		try {
			synchronized (this) {
				if(!exist(path,false))
				ZKClient.create(path, mode, t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发布服务失败,节点路径为："+path);
		}
	}
	private boolean exist(String path, boolean useWatcher){
		try {
			return ZKClient.exist(path,useWatcher);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用zookeeper失败", e);
			return false;
		}
	}
	protected IService getService(String name, boolean enable) {
		String path=Service.getServicePath(name);
		
		try {
			Map<String, String> configure=ZKClient.<Map<String, String>>get(path,true);
			Service service=new Service();
			service.setName(name);
			service.setStrategy(configure);
			return service;
		} catch (Exception e) {
			logger.error("获取zookeeper对应的服务失败，服务名称为："+name,e);
			e.printStackTrace();
		}
		return null;
	}
	
	private void deleteNode(String path) {
		try {
			if(exist(path, false))ZKClient.delete(path);
		} catch (Exception e) {
			logger.error("删除zookeeper节点失败",e);
			e.printStackTrace();
		}
	}
	public List<String> getChildren(String path){
		try {
			return ZKClient.getChildren(path,true);
		} catch (Exception e) {
			logger.error("获取children失败，path="+path,e);
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	/**
	 * 获取children的节点信息
	 * @param children 节点path
	 * @return
	 */
	private List<IServiceNode> getServiceNodes(IService service, String servicePath, List<String> children, boolean useWatch) {
		List<IServiceNode> nodes=new ArrayList<IServiceNode>();
		for(String subPath:children){
			ServiceNode node=new ServiceNode(subPath);
			String path=PathUtil.getServiceSubNodePath(servicePath, subPath);
			Map<String, String> map=getNodeValue(path,useWatch);
			node.setStrategy(map);
			node.setService(service);
			nodes.add(node);
		}
		return nodes;
	}
	
	private Map<String, String> getNodeValue(String path,boolean useWatch){
		try {
			Map<String, String> map = ZKClient.<Map<String, String>>get(path,useWatch);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteService(String name) {
		String enableChildrenPath=Service.getEnablePath(name);
		String disableChildrenPath=Service.getDisablePath(name);
		deleteChildren(name,enableChildrenPath);
		deleteChildren(name, disableChildrenPath);
		deleteNode(Service.getServicePath(name));
		
	}
	private void deleteChildren(String name, String path){
		List<String> children=getChildren(path);
		for(String identity: children){
			String childrenPath=PathUtil.getServiceSubNodePath(path, identity);
			deleteNode(childrenPath);
		}
		deleteNode(path);
	}
	@Override
	public void listernService(IService service) {
		logger.debug("开始监听service："+service.toString());
		getService(service.getName());
	}
	@Override
	public void listernServiceNode(IServiceNode serviceNode) {
		logger.debug("开始监听node："+serviceNode.toString());
		IService service=serviceNode.getService();
		this.getSeviceNode(service.getName(), serviceNode.getIdentity(), true, true);
	}
}
