package com.xiaoheiwu.service.serializer.meta.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;


public class ByteMeta implements IObjectMeta<Byte> {

	@Override
	public Byte read(DataInput dataInput) {
		return dataInput.readByte();
	}

	@Override
	public void write(Byte t,DataOutput output) {
		output.resizeIfNecessary(1);
		output.writeByte(t);
	}

	@Override
	public DataType getDataType() {
		return DataType.BYTE;
	}

	@Override
	public String getRawClassName() {
		return Byte.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.BYTE.getTypeValue());
	}

}
