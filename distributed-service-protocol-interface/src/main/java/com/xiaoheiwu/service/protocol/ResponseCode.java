package com.xiaoheiwu.service.protocol;

import java.util.HashMap;
import java.util.Map;

public enum ResponseCode {
	NONE(-1),SUCCESS(0),LOCAL_EXCEPTION(1),REMOTO_EXCEPTION(2),TIMEOUT_EXCEPTION(3),DEGRADE_EXCEPTION(4),LIMIT_EXCEPTION(5),STATUS_EXCEPTION(6);
	private int code;
	private static Map<Integer, ResponseCode> codes=new HashMap<Integer, ResponseCode>();
	private ResponseCode(int code){
		this.code=code;
	}
	public int getCode(){
		return this.code;
	}
	static {
		codes.put(0, SUCCESS);//处理成功
		codes.put(1, LOCAL_EXCEPTION);//客户端调用出错
		codes.put(2, REMOTO_EXCEPTION);//服务器调用异常
		codes.put(3, TIMEOUT_EXCEPTION);//超时异常
		codes.put(4, DEGRADE_EXCEPTION);//降级异常，在服务器能力不足时，客户端做的限流措施
		codes.put(5, LIMIT_EXCEPTION);//限流异常，在服务器能力不足时，服务器端做的限流措施
		codes.put(6, STATUS_EXCEPTION);//限流异常，在服务器能力不足时，服务器端做的限流措施
		
	}
	public static ResponseCode getResponseCode(int code){
		return codes.get(code);
	}
}
