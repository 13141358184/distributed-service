package com.xiaoheiwu.service.hello_api;

import com.xiaoheiwu.service.annotation.Service;

@Service("HelloAPI")
public interface HelloAPI {

	public String hello(String name, String person) ;
	
	public String hello(String name) ;
}
