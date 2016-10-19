package com.xiaoheiwu.service.hello_service;

import com.xiaoheiwu.service.hello_api.HelloAPI;


public class HelloWorldImpl implements HelloAPI{
	
	@Override
	public String hello(final String name) {
		
		return "hello "+name;
	}
//	public static void main(String[] args) {
//		ComponentProvider.addPackage("com.xiaoheiwu.service");
//		ComponentProvider.addPackage("com.chris.service");
//		Bootstrap server=new Bootstrap(8087);
//		server.start();
////		server.registe(new HelloWorldImpl());
//	}
//	protected Person createPerson(String name, long i){
//		Person person=new Person();
//		person.setName(name+i);
//		List<Map<String, Long>> list=new ArrayList<Map<String,Long>>();
//		Map<String, Long> map=new HashMap<String, Long>();
//		String key="sb";
//		map.put(i+key, i);
//		map.put((i+1)+key, i+1);
//		map.put((i+2)+key, i+2);
//		Map<String, Long> map1=new HashMap<String, Long>();
//		i=i+10;
//		map1.put(i+key, i);
//		map1.put((i+1)+key, i+1);
//		map1.put((i+2)+key, i+2);
//		Map<String, Long> map2=new HashMap<String, Long>();
//		i=i+100;
//		map2.put(i+key, i);
//		map2.put((i+1)+key, i+1);
//		map2.put((i+2)+key, i+2);
//		list.add(map);
//		list.add(map1);
//		list.add(map2);
//		person.setList(list);
//		return person;
//		
//	}
	@Override
	public String hello(String name, String person) {
		return name+":"+person;
	}
	
}
