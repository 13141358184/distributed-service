package com.xiaoheiwu.service.container;

import java.lang.reflect.InvocationTargetException;

import com.xiaoheiwu.service.container.container.impl.ServiceContainer;

import junit.framework.TestCase;

public class ServiceContainerTest extends TestCase{
	
	public void testLoadService() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException{
		ServiceContainer container=new ServiceContainer("D:\\tmp\\services", "HelloWorld",8087);
		Class clazz=container.loadServiceClass("com.xiaoheiwu.service.hello_api.Hello");
		Object o=clazz.getMethod("print").invoke(clazz.newInstance());
		System.out.println(clazz.getName());
		Thread.sleep(100000);
		System.out.println("start execute.....");
		clazz=container.loadServiceClass("com.xiaoheiwu.service.hello_api.Hello");
		o=clazz.getMethod("print").invoke(clazz.newInstance());
		System.out.println(clazz.getName());
	}
}
