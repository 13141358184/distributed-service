package com.xiaoheiwu.service.serializer.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public interface IObjectMeta<T> {

	public T read(DataInput dataInput);

	public void write(T t, DataOutput output);
	
	public void writeMeta(DataOutput output);

	DataType getDataType();

	String getRawClassName();

}
