package com.xiaoheiwu.service.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.manager.impl.Service;
import com.xiaoheiwu.service.manager.impl.ServiceNode;
import com.xiaoheiwu.service.manager.impl.ZookeeperServiceManager;
import com.xiaoheiwu.service.zookeeper.ZKClient;
import com.xiaoheiwu.service.zookeeper.util.PathUtil;

import junit.framework.TestCase;

public class ServiceManagerPublishTest extends TestCase {
	private IServiceManager manager=ComponentProvider.getInstance(ZookeeperServiceManager.class);
	
	
	public void testPublishService() throws Exception{
//		Service service=new Service();
//		service.setName(ICommandService.class);
//		service.setVersion("2.0");
//		Map<String, String> strategy=new HashMap<String, String>();
//		strategy.put("h", "d");
//		service.setStrategy(strategy);
//		ServiceNode serverNode=new ServiceNode("192.168.1.123",8087);
//		serverNode.setStatus(1);
//		strategy.put("wangbada", "dddd");
//		serverNode.setStrategy(strategy);
//		deleteServiceAndChildren(service);
//		manager.publishServiceNode(service, serverNode);
//		IService service2=manager.getService(ICommandService.class);
//		System.out.println(service2.toString());
//		List<IServiceNode> nodes=manager.getSeviceNodes(service.getName());
//		for(IServiceNode node:nodes){
//			System.out.println(node.toString());
//		}
	}
	private void deleteServiceAndChildren(IService service) throws Exception {
		String path=Service.getEnablePath(service.getName());
		List<String> children =ZKClient.getChildren(path,true);
		for(String p:children){
			ZKClient.delete(PathUtil.getServiceSubNodePath(path, p));
		}
		ZKClient.delete(path);
	}
}
