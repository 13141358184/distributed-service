package com.xiaoheiwu.service.server.handle.service.limit;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.governance.IRequestAccess;
import com.xiaoheiwu.service.server.handle.service.RequestServiceGovernance;
import com.xiaoheiwu.service.server.protocol.IServerRequest;


public class LimitServiceGovernance extends RequestServiceGovernance {

	@Override
	public ServiceConfigureKey getConfigureKey() {
		return ServiceConfigureKey.LIMIT;
	}


	@Override
	protected IRequestAccess<String, IServerRequest> newRequestAccess(
			String serviceName, IServiceNode node) {
		RequestLimit limit= new RequestLimit();
		return limit;
	}




	
}
