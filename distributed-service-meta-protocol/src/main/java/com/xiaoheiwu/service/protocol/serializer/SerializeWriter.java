package com.xiaoheiwu.service.protocol.serializer;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.protocol.ProtocolHeader;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class SerializeWriter implements ISerializeWriter{
	private ISerializer serializer=ComponentProvider.getInstance(MetaSerializer.class);
	@Override
	public byte[] doCall(ProtocolHeader header, IServiceRequest call) {
		DataOutput output=serializer.serialize(header);
		DataOutput callOutput=serializer.serialize(call);
		output.writeDataOutput(callOutput);
		return output.getData();
	}

	

	@Override
	public byte[] doReturn(IServiceResponse value) {
		DataOutput output=serializer.serialize(value);
		return output.getData();
	}


}
