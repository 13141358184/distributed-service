package com.xiaoheiwu.service.transport;

import java.util.Map;

public interface ITransportArgs {
	public static final int SOCKET_TRANSPORT=0;//socket transport
	public static final int FILE_TRANSPORT=1;//File transport
	public static final int JVM_TRANSPORT=2;//同jvm内进程调用
	public static final int HOST_TRANSPORT=3;//同一主机方法调用
	
	public int getTransportType();//transportType
	
	public String getIdentity();
	
	public Map<String, Object> getArgs();
	
}
