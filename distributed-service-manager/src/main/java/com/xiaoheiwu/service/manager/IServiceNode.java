package com.xiaoheiwu.service.manager;

import java.util.Map;

import com.xiaoheiwu.service.common.configure.IConfigure;




public interface IServiceNode{
	
	public String getIp();
	
	public int getPort();
	
	public String getIdentity();
	
	public int getStatus();
	
	public IService getService();
	
	/**
	 * 获取key对应的配置信息。如果key同时存在service和node中，node中的会覆盖service中的配置
	 * @param key 
	 * @return
	 */
	public String getConfigure(String key);
	
	public IConfigure getConfigure();
	
	public Map<String, String> getStrategy();
	
	
}
