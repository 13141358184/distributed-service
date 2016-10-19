package com.xiaoheiwu.service.protocol;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.protocol.IProtocolService;
import com.xiaoheiwu.service.protocol.impl.ServiceRequest;
import com.xiaoheiwu.service.protocol.impl.ServiceResponse;
import com.xiaoheiwu.service.protocol.serializer.ISerializeReader;
import com.xiaoheiwu.service.protocol.serializer.ISerializeWriter;
import com.xiaoheiwu.service.protocol.serializer.SerializeReader;
import com.xiaoheiwu.service.protocol.serializer.SerializeWriter;
@Service
@ServiceDefault(IProtocolService.class)
public class MetaProtocolService implements IProtocolService{
	private ISerializeWriter write=new SerializeWriter();
	private ISerializeReader reader=new SerializeReader();
	private final static byte HEART_BEAT=26;
	@Override
	public byte[] doCall(IServiceRequest call) {
		ProtocolHeader header=new ProtocolHeader();
		return write.doCall(header, call);
	}

	@Override
	public ServiceResponse doReturn(byte[] data) {
		return reader.doReturn(data);
	}

	@Override
	public ServiceRequest doCall(byte[] data) {
		return reader.doCall(data);
	}

	@Override
	public byte[] doReturn(IServiceResponse value) {
		return write.doReturn(value);
	}

	@Override
	public byte getIndex() {
		return 0;
	}

	public static void main(String[] args) {
		ServiceRequest call=new ServiceRequest();
		call.setMethodName("hello");
		call.setServiceCallId(1);
		call.setServiceName("com.chris.service.HelloWorld");
		Object[]  parameters=new Object[3];
		parameters[0]="134";
		parameters[1]=97;
		parameters[2]="wangbadan";
		call.setParameters(parameters);
		call.setProtocolId((byte)1);
		MetaProtocolService service=new MetaProtocolService();
		byte[] data=service.doCall(call);
		ServiceRequest tmp=service.doCall(data);
		System.out.println(tmp.toString());
	}

	@Override
	public boolean isBeatHeart(byte[] data) {
		if(data.length==1&&data[0]==HEART_BEAT)return true;
		return false;
	}

	@Override
	public byte[] createBeatHeart() {
		byte[] data=new byte[1];
		data[0]=HEART_BEAT;
		return data;
	}
}
