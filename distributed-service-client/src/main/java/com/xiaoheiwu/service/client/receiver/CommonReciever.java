package com.xiaoheiwu.service.client.receiver;

import com.xiaoheiwu.service.client.event.CallEvent;
import com.xiaoheiwu.service.client.event.ClientEventType;
import com.xiaoheiwu.service.client.protocol.IClientResponse;

public class CommonReciever implements IReceiver{
	public IReceiver receiver;
	public CommonReciever(IReceiver receiver){
		this.receiver=receiver;
	}
	@Override
	public void doRecieve(IClientResponse response) {
		this.receiver.doRecieve(response);
		CallEvent.fireEvent(ClientEventType.CALL_RECEIVE_EVENT, response);
	}

}
