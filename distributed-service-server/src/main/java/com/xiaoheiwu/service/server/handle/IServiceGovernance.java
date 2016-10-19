package com.xiaoheiwu.service.server.handle;

import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
public interface IServiceGovernance extends IRequestHandle{

	/**
	 * 当配置发生变化时，更新数据
	 * @param value
	 */
	public void updateConfigureInfo(String serviceName, String value);
	
	/**
	 * 
	 * @return 这个服务治理对应的配置key
	 */
	public ServiceConfigureKey getConfigureKey();
}
