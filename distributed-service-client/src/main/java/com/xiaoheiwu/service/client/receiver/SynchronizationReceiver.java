package com.xiaoheiwu.service.client.receiver;

import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.common.log.Log;

public class SynchronizationReceiver implements IReceiver{
	private Log logger=Log.getLogger(this.getClass());
	@Override
	public void doRecieve(IClientResponse response) {
		synchronized (response) {
			response.notify();
		}
	}

}
