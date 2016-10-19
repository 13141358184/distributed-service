package com.xiaoheiwu.service.serializer.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.xiaoheiwu.service.serializer.meta.FieldMeta;


public class MethodUtil {
	public static String getGetMethod(String fieldName){
		return "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}
	
	public static String getSetMethod(String fieldName){
		return "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}
	
	public static Object getFieldValue(Object object,String name) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String getMethodName = com.xiaoheiwu.service.serializer.util.MethodUtil.getGetMethod(name);
		Method method = object.getClass().getMethod(getMethodName);
		Object value = method.invoke(object);
		return value;
	}
	
	public static void setValue2Object(String fieldName,Class valueClazz,Object value, Object object) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String setMethodName = com.xiaoheiwu.service.serializer.util.MethodUtil.getSetMethod(fieldName);
		Method method = object.getClass().getMethod(setMethodName,
				valueClazz);
		method.invoke(object, value);
	}
	public static String getVarName(com.xiaoheiwu.service.serializer.datatype.DataType dataType){
		String name=dataType.getDataTypeClass().getSimpleName();
		return name.substring(0,1).toLowerCase()+name.substring(1);
	}
}
