package com.xiaoheiwu.service.serializer.meta;

import com.xiaoheiwu.service.serializer.datatype.DataType;

public interface IObjectMetaProductor extends IMetaProductor{
	public IObjectMeta createObjectMeta(String name, IMetaFactory factory);
}
