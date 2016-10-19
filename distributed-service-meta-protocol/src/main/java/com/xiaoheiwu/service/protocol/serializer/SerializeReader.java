package com.xiaoheiwu.service.protocol.serializer;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.ProtocolHeader;
import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;

public class SerializeReader implements ISerializeReader{
	private ISerializer serializer=ComponentProvider.getInstance(MetaSerializer.class);

	@Override
	public ServiceResponse doReturn(byte[] data) {
		DataInput input=new ByteDataInput(data);
		ServiceResponse value=(ServiceResponse)serializer.deserialize(input);
		return value;
	}

	@Override
	public ServiceRequest doCall(byte[] data) {
		DataInput input=new ByteDataInput(data);
		ProtocolHeader header=(ProtocolHeader)serializer.deserialize(input);
		ServiceRequest call=(ServiceRequest)serializer.deserialize(input);
		return call;
	}

}
