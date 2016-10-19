package com.xiaoheiwu.service.client.event;

public enum ClientEventType {
	CALL_SEND_EVENT,CALL_RECEIVE_EVENT,QUEUE_SENDER_EVENT,RETRY_SENDER_EVENT,TRANSPORT_SENDER_EVENT,TRANSACTION_EVENT,SYSTEM_SERVICE_INIT_EVENT;
	
	public String getEvent(){
		return this.name();
	}
}
