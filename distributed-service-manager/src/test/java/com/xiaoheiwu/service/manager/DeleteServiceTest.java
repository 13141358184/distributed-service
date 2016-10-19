package com.xiaoheiwu.service.manager;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.manager.impl.ZookeeperServiceManager;

import junit.framework.TestCase;

public class DeleteServiceTest extends TestCase{
	private IServiceManager manager=ComponentProvider.getInstance(ZookeeperServiceManager.class);
	public void testDelete(){
		manager.deleteService("HelloAPI");
		manager.deleteService("HellowWorld2");
		manager.deleteService("CommandService");
		manager.deleteService("service");
	}
}
