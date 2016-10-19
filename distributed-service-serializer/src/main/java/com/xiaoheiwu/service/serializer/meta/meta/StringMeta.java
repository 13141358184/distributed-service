package com.xiaoheiwu.service.serializer.meta.meta;


import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class StringMeta implements IObjectMeta<String>{
	
	@Override
	public String read(DataInput dataInput) {
		return dataInput.readString();
	}

	@Override
	public void write(String v, DataOutput output) {
		output.writeString(v);
	}

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String getRawClassName() {
		return String.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.STRING.getTypeValue());
	}
}
