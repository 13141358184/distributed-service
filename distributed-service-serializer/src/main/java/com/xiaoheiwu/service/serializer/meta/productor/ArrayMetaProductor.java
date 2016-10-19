package com.xiaoheiwu.service.serializer.meta.productor;

import java.util.Arrays;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.ArrayMeta;

public class ArrayMetaProductor extends BaseTypeMetaProductor{
	private ArrayMeta meta= new ArrayMeta();
	@Override
	public IObjectMeta createObjectMeta(Class clazz) {
		return meta;
	}


	@Override
	public DataType getDataType(Class clazz) {
		if(clazz.isArray()||DataType.ARRAY.getDataTypeClass().equals(clazz))return DataType.ARRAY;
		return null;
	}

	public static void main(String[] args) {
		String[]  t=new String[3];
		ArrayMetaProductor arrayMetaProductor=new ArrayMetaProductor();
		DataType dataType=arrayMetaProductor.getDataType(t.getClass());
		System.out.println(dataType.getDataTypeClass());
	}
}
