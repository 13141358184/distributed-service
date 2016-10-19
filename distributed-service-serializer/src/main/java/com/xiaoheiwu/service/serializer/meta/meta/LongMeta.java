package com.xiaoheiwu.service.serializer.meta.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class LongMeta implements IObjectMeta<Long>{

	@Override
	public Long read(DataInput dataInput) {
		return dataInput.readLong();
	}

	@Override
	public void write(Long t, DataOutput output) {
		output.resizeIfNecessary(4);
		output.writeLong(t);
	}

	@Override
	public DataType getDataType() {
		return DataType.LONG;
	}

	@Override
	public String getRawClassName() {
		return Long.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.LONG.getTypeValue());
	}
}
