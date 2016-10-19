package com.xiaoheiwu.service.serializer.meta.productor;

import java.util.List;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.ListMeta;

public class ListMetaProductor extends BaseTypeMetaProductor{
	private ListMeta listMeta=new ListMeta();
	@Override
	public IObjectMeta createObjectMeta(Class clazz){
		return listMeta;
	}

	@Override
	public DataType getDataType(Class clazz) {
		if(List.class.isAssignableFrom(clazz))return DataType.LIST;
		return null;
	}

}
