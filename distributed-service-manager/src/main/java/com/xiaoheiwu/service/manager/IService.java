package com.xiaoheiwu.service.manager;

import java.util.Map;

/**
 * 注册服务需要的元数据
 * @author Chris
 *
 */
public interface IService {
	public static final int ONLINE=0;
	public static final int OFFLINE=-1;
	public static final String SERVICE_LIMIT="SERVICE_LIMIT";
	String getName();//服务名称
	
	Map<String, String> getStrategy();//所有服务的公用配置
	
	
}
