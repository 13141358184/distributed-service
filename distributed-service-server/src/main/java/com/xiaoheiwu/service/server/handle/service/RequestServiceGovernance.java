package com.xiaoheiwu.service.server.handle.service;

import com.xiaoheiwu.service.manager.governance.AbstractServiceGovernance;
import com.xiaoheiwu.service.manager.governance.IRequestAccess;
import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.server.handle.IRequestHandle;
import com.xiaoheiwu.service.server.protocol.IServerRequest;

public abstract class RequestServiceGovernance extends AbstractServiceGovernance<IRequestAccess<String, IServerRequest> ,IServerRequest > implements IRequestHandle{


	@Override
	protected String getSeviceName(IServerRequest request) {
		if(request==null)return null;
		return request.getServiceName();
	}

	@Override
	protected void doRejectHandle(IServerRequest request) {
		String serviceName=request.getServiceName();
		IRequestAccess<String, IServerRequest> access=getRequestAccess(serviceName);
		ResponseCode code=access.getExceptionCode();
		log.warn(code.name(),getSeviceName(request),request.getMethodName(),request.getServiceCallId());
		request.sendErrorResponse(code,code.name());
	}
}
