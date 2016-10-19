package com.xiaoheiwu.service.serializer.meta.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public abstract class AbstractCollectionMeta<T> implements IObjectMeta<T>{

	protected IObjectMeta readElementMeta(DataInput dataInput) {
		byte typeValue=dataInput.readByte();
		DataType dataType=DataType.getType(typeValue);
		IObjectMeta paradigmTmp=dataType.getMetaProductor().createObjectMeta(dataType,dataInput);
		return paradigmTmp;
	}
	protected IObjectMeta writeElementMeta(Object object,DataOutput output) {
		DataType dataType=DataType.getDataType(object.getClass());
		IObjectMeta paradigmTmp = dataType.getMetaProductor().createObjectMeta(object.getClass());
		paradigmTmp.writeMeta(output);
		return paradigmTmp;
	}

}
