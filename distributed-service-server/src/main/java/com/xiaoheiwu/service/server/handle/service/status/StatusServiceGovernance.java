package com.xiaoheiwu.service.server.handle.service.status;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.governance.IRequestAccess;
import com.xiaoheiwu.service.manager.governance.imp.AbstractRequestAccess;
import com.xiaoheiwu.service.manager.impl.Service;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.server.handle.service.RequestServiceGovernance;
import com.xiaoheiwu.service.server.protocol.IServerRequest;

public class StatusServiceGovernance extends RequestServiceGovernance{
	
	@Override
	public ServiceConfigureKey getConfigureKey() {
		return ServiceConfigureKey.STATUS;
	}


	@Override
	protected IRequestAccess<String, IServerRequest> newRequestAccess(
			String serviceName, IServiceNode node) {
		return new AbstractRequestAccess<IServerRequest>() {
			@Override
			public boolean access(IServerRequest request) {
				Integer s=convertInterger();
				if(s==null)return true;
				if(Service.OFFLINE==s){
					return false;
				}
				return true;
			}

			@Override
			public  ResponseCode getExceptionCode() {
				return ResponseCode.STATUS_EXCEPTION;
			}
		};
	}





}
