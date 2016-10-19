package com.xiaoheiwu.service.hello_client;


import com.xiaoheiwu.service.client.services.ServiceFactory;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.hello_api.HelloAPI;



public class HelloClient{
	
	public static void main(String[] args) throws InterruptedException {
		ComponentProvider.startComponent();
		HelloClient client=new HelloClient();
		HelloAPI helloWorld=ServiceFactory.getInstance().getService(HelloAPI.class);
		Object result=helloWorld.hello("yuanxiaodong123");
		System.out.println(result);
		
//		Thread.sleep(15000);
//		result=helloWorld.hello("yuanxiaodong123","wbd");
//		System.out.println(result);
//		result=helloWorld.hello("yuanxiaodong123");
//		System.out.println(result);
//		HelloWorld hw=ServiceFactory.getInstance().getService(HelloWorld.class);
//		result=hw.helloWorld("yuanxiaodong");
//		System.out.println(result);
		
	}

}
