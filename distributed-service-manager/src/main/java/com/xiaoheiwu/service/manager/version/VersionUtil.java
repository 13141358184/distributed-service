package com.xiaoheiwu.service.manager.version;

import com.xiaoheiwu.service.manager.context.ApplicationContext;

public class VersionUtil {
	private static ApplicationContext context=ApplicationContext.getInstance();
	public static void putVersion(String serviceName, String version){
		context.put(getVersionKey(serviceName), version);
	}
	
	public static String getVersion(String serviceName){
		return context.getStringValue(getVersionKey(serviceName));
	}
	
	private static String getVersionKey(String serviceName) {
		return serviceName+".version";
	}
}
