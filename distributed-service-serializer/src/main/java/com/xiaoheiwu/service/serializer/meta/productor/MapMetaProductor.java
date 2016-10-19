package com.xiaoheiwu.service.serializer.meta.productor;


import java.util.List;
import java.util.Map;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.MapMeta;


public class MapMetaProductor  extends BaseTypeMetaProductor{
	private MapMeta mapMeta=new MapMeta();
	@Override
	public IObjectMeta createObjectMeta(Class clazz){
		return mapMeta;
	}
	@Override
	public DataType getDataType(Class clazz) {
		if(Map.class.isAssignableFrom(clazz))return DataType.MAP;
		return null;
	}
}
