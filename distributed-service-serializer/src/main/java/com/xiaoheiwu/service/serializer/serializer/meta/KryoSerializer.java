package com.xiaoheiwu.service.serializer.serializer.meta;


import org.springframework.stereotype.Service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.xiaoheiwu.service.serializer.AbstractSerializer;
import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataOutput;
@Service
public class KryoSerializer extends AbstractSerializer{
	private Kryo kryo=new Kryo();
	@Override
	public boolean isSerializable(Object object) {
		return true;
	}

	@Override
	public DataOutput serialize(Object object)
			throws NotSupportSerializerException {
		Output output =new Output(1000,Integer.MAX_VALUE);
		kryo.writeClassAndObject(output, object);         
		return ByteDataOutput.wrap(output.getBuffer());
	}

	@Override
	public Object deserialize(DataInput input)
			throws NotSupportSerializerException {
		Input in = new Input(input.getOrigData());
		return kryo.readClassAndObject(in);
	}



	@Override
	public Class getDataTypeClass(Class clazz) {
		return DataType.getDataType(clazz).getDataTypeClass();
	}

}
