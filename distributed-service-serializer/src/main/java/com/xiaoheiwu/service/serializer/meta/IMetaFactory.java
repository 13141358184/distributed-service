package com.xiaoheiwu.service.serializer.meta;

public interface IMetaFactory<T> {

	
	public IObjectMeta<T> getMeta(Class clazz);	
	
}
