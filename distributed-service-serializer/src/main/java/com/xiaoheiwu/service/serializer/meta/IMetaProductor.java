package com.xiaoheiwu.service.serializer.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.stream.DataInput;


public interface IMetaProductor {

	public IObjectMeta createObjectMeta(Class clazz);
	
	public IObjectMeta createObjectMeta(DataType type,DataInput dataInput);
	
	public DataType getDataType(Class clazz);
}
