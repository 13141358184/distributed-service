package com.yuanxiaodong.chris;


import com.xiaoheiwu.service.hello_api.HelloAPI;
import com.xiaoheiwu.service.hello_api.HelloWorld;

public class HelloWorld2 implements HelloWorld{

	@Override
	public String helloWorld(String name) {
//		HelloAPI helloAPI=ServiceFactory.getInstance().getService(HelloAPI.class);
//		helloAPI.hello("woshidi3ceng", "yuanxiaodong");
		return "wbd"+name;
	}
//	public static void main(String[] args) {
//		ComponentProvider.addPackage("com.xiaoheiwu.service");
//		ComponentProvider.addPackage("com.chris.service");
//		ComponentProvider.addPackage("com.yuanxiaodong.chris");
//		Bootstrap server=new Bootstrap(8087);
//		server.start();
////		server.registe(new HelloWorldImpl());
//	}
}
