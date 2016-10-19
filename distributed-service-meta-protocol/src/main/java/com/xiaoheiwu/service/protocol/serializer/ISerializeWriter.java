package com.xiaoheiwu.service.protocol.serializer;

import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ProtocolHeader;

public interface ISerializeWriter {
	
	public byte[] doCall(ProtocolHeader header, IServiceRequest call);
	
	
	public byte[] doReturn(IServiceResponse value);
	
}
