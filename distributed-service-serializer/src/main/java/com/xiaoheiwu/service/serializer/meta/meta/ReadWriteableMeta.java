package com.xiaoheiwu.service.serializer.meta.meta;

import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.serializer.readwriteable.IReadWriteable;
import com.xiaoheiwu.service.serializer.serializer.readwriteable.ReadWriteableSerializer;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class ReadWriteableMeta implements IObjectMeta<IReadWriteable>{
	private ISerializer serializer=new ReadWriteableSerializer();
	public ReadWriteableMeta(){
		
	}
	public ReadWriteableMeta(ISerializer serializer){
		this.serializer=serializer;
	}
	@Override
	public IReadWriteable read(DataInput dataInput) {
		return (IReadWriteable)serializer.deserialize(dataInput);
	}

	@Override
	public void write(IReadWriteable t, DataOutput output) {
		DataOutput out = serializer.serialize(t);
		output.writeBytes(out.getData());
	}

	@Override
	public DataType getDataType() {
		return DataType.READWRITEABLE;
	}

	@Override
	public String getRawClassName() {
		return IReadWriteable.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.READWRITEABLE.getTypeValue());
	}
}
