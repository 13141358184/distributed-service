package com.xiaoheiwu.service.serializer.meta.productor;

import java.util.Map;
import java.util.Set;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.SetMeta;


public class SetMetaProductor  extends BaseTypeMetaProductor {
	private SetMeta meta=new SetMeta();
	@Override
	public IObjectMeta createObjectMeta(Class clazz){
		return meta;
	}
	
	@Override
	public DataType getDataType(Class clazz) {
		if(Set.class.isAssignableFrom(clazz))return DataType.SET;
		return null;
	}
}
