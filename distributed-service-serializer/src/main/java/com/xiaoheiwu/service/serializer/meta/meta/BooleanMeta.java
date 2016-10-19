package com.xiaoheiwu.service.serializer.meta.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class BooleanMeta implements IObjectMeta<Boolean>{

	@Override
	public Boolean read(DataInput dataInput) {
		return dataInput.readBoolean();
	}

	@Override
	public void write(Boolean b, DataOutput output) {
		output.writeBoolean(b);
	}

	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}

	@Override
	public String getRawClassName() {
		return Boolean.class.getName();
	}

	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.BOOLEAN.getTypeValue());
	}


}
