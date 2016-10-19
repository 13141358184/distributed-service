package com.xiaoheiwu.service.server.handle.requesthandle;

import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.manager.pipline.Pipline;
import com.xiaoheiwu.service.server.handle.IPiplineRequestHandle;
import com.xiaoheiwu.service.server.handle.IRequestHandle;
import com.xiaoheiwu.service.server.protocol.IServerRequest;
public class PiplineRequestHandle  extends Pipline<IRequestHandle> implements IPiplineRequestHandle{
	private Log logger=Log.getLogger(this.getClass());
	
	@Override
	public boolean handle(final IServerRequest request) {
		executeRequest(request);
		return true;
	}
	
	/**
	 * 执行pipline所有的节点
	 * @param request reqeust
	 * @return
	 */
	private boolean executeRequest(IServerRequest request) {
		try{
			for(IRequestHandle handle: requestHandles){
				boolean continueHandle=handle.handle(request);
				if(!continueHandle)return continueHandle;
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			request.sendErrorResponse(e.getMessage());
			return false;
		}
		
	}
}
