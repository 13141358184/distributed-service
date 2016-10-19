package com.xiaoheiwu.service.serializer.meta.meta;


import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class DoubleMeta implements IObjectMeta<Double>{

	@Override
	public Double read(DataInput dataInput) {
		return dataInput.readDouble();
	}

	@Override
	public void write(Double t, DataOutput output) {
		output.resizeIfNecessary(8);
		output.writeDouble(t);
		
	}

	@Override
	public DataType getDataType() {
		return DataType.DOUBLE;
	}

	@Override
	public String getRawClassName() {
		return Double.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.DOUBLE.getTypeValue());
	}
}
