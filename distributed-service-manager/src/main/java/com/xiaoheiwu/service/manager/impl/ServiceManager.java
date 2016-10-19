package com.xiaoheiwu.service.manager.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.annotation.util.ServiceUtils;
import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceManager;
import com.xiaoheiwu.service.manager.event.ChildrenChangeEvent;
import com.xiaoheiwu.service.manager.event.NodeChangeEvent;
import com.xiaoheiwu.service.manager.event.ServiceChangeEvent;
import com.xiaoheiwu.service.zookeeper.ZKClient;
import com.xiaoheiwu.service.zookeeper.ZKEvent;

@Service
@ServiceDefault(IServiceManager.class)
public class ServiceManager implements IServiceManager,IListerner{
	private Log logger=Log.getLogger(this.getClass());
	private IServiceManager localserviceManager=ComponentProvider.getInstance(LocalServiceManager.class);
	private IServiceManager zookeepServiceManager=ComponentProvider.getInstance(ZookeeperServiceManager.class);
	public ServiceManager(){
		Event.addListerner(ZKEvent.NodeChildrenChanged, this);
		Event.addListerner(ZKEvent.NodeCreated, this);
		Event.addListerner(ZKEvent.NodeDataChanged, this);
		Event.addListerner(ZKEvent.NodeDeleted, this);
		logger.debug("ZKEVENTListerner","ALL");
	}
	
	@Override
	public IService getService(String name) {
		IService service=localserviceManager.getService(name);
		if(service==null){
			synchronized (this) {
				service=localserviceManager.getService(name);
				if(service==null){
					service=zookeepServiceManager.getService(name);
					publishServices2Local(service);
				}
				
			}
		}
		return service;
	}

	@Override
	public IServiceNode publishServiceNode(IService service, IServiceNode node){
		return publishServiceNode(service, node, true);
	}
	@Override
	public IServiceNode publishServiceNode(IService service, IServiceNode node, boolean enable) {
		synchronized (this) {
			localserviceManager.publishServiceNode(service,node,enable);
			node=zookeepServiceManager.publishServiceNode(service,node, enable);
		}
		return node;
	}
	@Override
	public List<IServiceNode> getSeviceNodes(String name){
		return this.getSeviceNodes(name, false);
	}
	@Override
	public List<IServiceNode> getSeviceNodes(String name, boolean listern) {
		List<IServiceNode> nodes=localserviceManager.getSeviceNodes(name,listern);
		if(nodes==null||nodes.size()==0){
			synchronized (this) {
				nodes=localserviceManager.getSeviceNodes(name,listern);
				if(nodes==null||nodes.size()==0){
					nodes=zookeepServiceManager.getSeviceNodes(name,listern);
					IService service=zookeepServiceManager.getService(name);
					publishServices2Local(service,nodes);
				}
			}
		}
		return nodes;
	}
	
	@Override
	public synchronized boolean disableServiceNode(String name, IServiceNode node) {
		boolean localSuccess=localserviceManager.disableServiceNode(name, node);
		boolean zookeeperSuccess=zookeepServiceManager.disableServiceNode(name, node);
		return localSuccess&&zookeeperSuccess;
	}
	@Override
	public synchronized boolean enableServiceNode(String name, IServiceNode node) {
		boolean localSuccess=localserviceManager.enableServiceNode(name, node);
		boolean zookeeperSuccess=zookeepServiceManager.enableServiceNode(name, node);
		return localSuccess&&zookeeperSuccess;
	}
	protected void publishServices2Local(IService service) {
		String name=service.getName();
		List<IServiceNode> nodes=zookeepServiceManager.getSeviceNodes(name);
		publishServices2Local(service, nodes);
		
	}
	protected void publishServices2Local(IService service, List<IServiceNode> nodes) {
		if(nodes==null||service==null)return;
		Iterator<IServiceNode> it = nodes.iterator();
		while(it.hasNext()){
			IServiceNode node=it.next();
			localserviceManager.publishServiceNode(service, node);
		}
	}
	@Override
	public void execute(IEvent event) {
		if(event!=null&&!ZKEvent.class.isInstance(event)){
			logger.error("不是zkEvent对象，"+event);
			return ;
		}
		ZKEvent zkEvent=(ZKEvent)event;
		doEvent(zkEvent);
	}
	@Override
	public void rePublishAllServiceNode(String name, List<IServiceNode> nodes){
		localserviceManager.rePublishAllServiceNode(name, nodes);
		zookeepServiceManager.rePublishAllServiceNode(name, nodes);
	}
	protected void doEvent(ZKEvent zkEvent) {
		logger.debug("SERVICE_MANGER_ZKEVENT",zkEvent.toString());
		String path=zkEvent.getPath();
		if(zkEvent.getEvent()==ZKEvent.NodeChildrenChanged){
			String name=com.xiaoheiwu.service.manager.impl.Service.parseServiceName(path);
			List<IServiceNode> nodes=zookeepServiceManager.getSeviceNodes(name);
			localserviceManager.rePublishAllServiceNode(name, nodes);
			new ChildrenChangeEvent(name, nodes).fireEvent();;
		}else if(zkEvent.getEvent()==ZKEvent.NodeDataChanged){
			boolean ischildrenNode=com.xiaoheiwu.service.manager.impl.Service.isChildrenPath(path);
			String serviceName=com.xiaoheiwu.service.manager.impl.Service.parseServiceName(path);
			if(ischildrenNode){
				String identity=com.xiaoheiwu.service.manager.impl.Service.parseServiceNodeIndentity(path);
				IServiceNode serverNode=this.zookeepServiceManager.getSeviceNode(serviceName, identity);
				new NodeChangeEvent(path, serviceName, serverNode).fireEvent();
			}else{
				IService service=this.zookeepServiceManager.getService(serviceName);
				new ServiceChangeEvent(path, serviceName, service).fireEvent();
			}
		}
	}
	@Override
	public void deleteService(String name) {
		this.localserviceManager.deleteService(name);
		this.zookeepServiceManager.deleteService(name);
	}
	@Override
	public IServiceNode getSeviceNode(String serviceName, String identity,
			boolean enable) {
		IServiceNode node=localserviceManager.getSeviceNode(serviceName, identity, enable);
		if(node==null){
			node=zookeepServiceManager.getSeviceNode(serviceName, identity, enable);
		}
		return node;
	}
	@Override
	public IServiceNode getSeviceNode(String serviceName, String identity) {
		return getSeviceNode(serviceName, identity, true);
	}
	@Override
	public void listernService(IService service) {
		this.zookeepServiceManager.listernService(service);
	}
	@Override
	public void listernServiceNode(IServiceNode serviceNode) {
		this.zookeepServiceManager.listernServiceNode(serviceNode);
	}

	

}
