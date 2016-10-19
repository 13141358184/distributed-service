package com.xiaoheiwu.service.manager.configure;

import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceNode;


/**
 * 获得服务的配置信息
 * @author Chris
 *
 */
public interface IServiceConfigure {
	/**
	 * 获取服务的配置信息
	 * @param key
	 * @return
	 */
	public IService getService(String serviceName);
	
	/**
	 * 获取服务节点的配置信息。服务的配置信息所有节点共享，节点的配置会覆盖服务的配置信息
	 * @param key
	 * @return
	 */
	public IServiceNode getServiceNode(String serviceName,String identity);
	

}
