package com.xiaoheiwu.service.protocol;

import java.util.Map;

public interface IServiceRequest {

	public String getServiceName();//服务名称
	
	public String getMethodName();//方法名称
	
	public long getServiceCallId();//调用序列号
	
	public Object[] getParameters();//获取调用参数
	
	public byte getProtocolId();//协议id
	
	public Map<String, Object> getAdditionalParameters();//附加参数，可以根据需要添加
	
	public IServiceRequest createSerializableRequest();
}
