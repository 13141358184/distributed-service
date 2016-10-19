package com.xiaoheiwu.service.protocol.serializer;

import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;

public interface ISerializeReader {
	

	public ServiceResponse doReturn(byte[] data);
	
	public ServiceRequest doCall(byte[] data);
	

}
