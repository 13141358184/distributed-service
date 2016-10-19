package com.xiaoheiwu.service.serializer.util;


import com.xiaoheiwu.service.serializer.datatype.DataType;

public class ClassUtil {

	public static Class getClass(Class clazz){
		DataType dataType= DataType.getDataType(clazz);
		if(dataType==null)return clazz;
		if(dataType==DataType.OBJECT)return clazz;
		if(dataType==dataType.ARRAY)return clazz;
		return dataType.getDataTypeClass();
	}
	public static void main(String[] args) {
		System.out.println(getClass(String.class));
	}
}
