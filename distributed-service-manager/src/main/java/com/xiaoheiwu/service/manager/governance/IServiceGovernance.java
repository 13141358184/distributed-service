package com.xiaoheiwu.service.manager.governance;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
public interface IServiceGovernance<R>{

	/**
	 * 当配置发生变化时，更新数据
	 * @param value
	 */
	public void updateConfigureInfo(String serviceName, IServiceNode node);
	
	/**
	 * 
	 * @return 这个服务治理对应的配置key
	 */
	public ServiceConfigureKey getConfigureKey();
	
	
	/**
	 * 业务执行接口，由业务实现。
	 * @param request
	 * @return
	 */
	public boolean handle(R request);
}
