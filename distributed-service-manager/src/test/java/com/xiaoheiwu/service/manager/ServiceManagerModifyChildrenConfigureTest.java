package com.xiaoheiwu.service.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.common.util.IPUtil;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.impl.Service;
import com.xiaoheiwu.service.manager.impl.ServiceManager;
import com.xiaoheiwu.service.manager.impl.ServiceNode;
import com.xiaoheiwu.service.manager.impl.ZookeeperServiceManager;
import com.xiaoheiwu.service.zookeeper.ZKClient;
import com.xiaoheiwu.service.zookeeper.ZKEvent;
import com.xiaoheiwu.service.zookeeper.util.PathUtil;

import junit.framework.TestCase;

public class ServiceManagerModifyChildrenConfigureTest extends TestCase{
	private IServiceManager manager=ComponentProvider.getInstance(ZookeeperServiceManager.class);
	private ServiceManager serviceManager=new ServiceManager();
	public void testChangeConfigure() throws Exception{
		IService service=manager.getService("HelloAPI");
		printMap(service.getStrategy());
		String childrenNodePath=PathUtil.getServiceSubNodePath(Service.getEnablePath(service.getName()), IPUtil.getIp()+":8087");
		System.out.println("childrenNodePath path "+childrenNodePath);
		Map<String, String> stragy=ZKClient.<Map<String, String>>get(childrenNodePath, true);
		printMap(stragy);
		addConfigure(stragy, ServiceConfigureKey.GRAY_PUBLISH_VERSION_MATCH.name(), ServiceConfigureKey.GRAY_PUBLISH_VERSION_MATCH.name()+":1.0.1");
//		addConfigure(stragy, ServiceConfigureKey.STATUS.name(), Service.OFFLINE+"");
		ZKClient.set(childrenNodePath, stragy);
		stragy=ZKClient.<Map<String, String>>get(childrenNodePath, true);
		printMap(stragy);
		Thread.sleep(100000000);
	}
	
	private void addConfigure(Map<String, String> stragy, String key,
			String value) {
		stragy.put(key, value);
	}

	private void printMap(Map<String, String> map){
		System.out.println("map size is "+map.size());
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry=it.next();
			System.out.println("    key is "+entry.getKey()+"; value is "+entry.getValue());
		}
	}
}
