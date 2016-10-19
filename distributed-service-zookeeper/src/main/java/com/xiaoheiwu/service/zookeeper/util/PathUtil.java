package com.xiaoheiwu.service.zookeeper.util;

public class PathUtil {

	public static String getServicePath(String serviceName){
		return "/"+serviceName;
	}
	
	
	public static String getServiceSubNodePath(String  path,String... subPaths){
		for(String subPath: subPaths){
			path+="/"+subPath;
		}
		return path;
	}
}
