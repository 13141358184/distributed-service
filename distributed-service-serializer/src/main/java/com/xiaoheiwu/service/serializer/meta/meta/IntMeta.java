package com.xiaoheiwu.service.serializer.meta.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class IntMeta implements IObjectMeta<Integer>{

	@Override
	public Integer read(DataInput dataInput) {
		return dataInput.readInt();
	}

	@Override
	public void write(Integer value, DataOutput output) {
		output.resizeIfNecessary(4);
		output.writeInt(value);
	}

	@Override
	public DataType getDataType() {
		return DataType.INT;
	}

	@Override
	public String getRawClassName() {
		return Integer.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.INT.getTypeValue());
	}
}
