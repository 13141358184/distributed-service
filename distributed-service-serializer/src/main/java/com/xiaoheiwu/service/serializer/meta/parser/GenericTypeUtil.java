package com.xiaoheiwu.service.serializer.meta.parser;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class GenericTypeUtil {
	
	public static boolean isGeneric(Type type) {
		if (ParameterizedType.class.isInstance(type)) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			if (parameterizedType.getActualTypeArguments().length > 0)
				return true;
			return false;
		}
		return false;
	}

	public static Type getMethodReturnType(Class clazz, String methodName,
			Class[] pclasses) {
		Method method;
		try {
			method = clazz.getMethod(methodName, pclasses);
			Type type = method.getGenericReturnType();
			return type;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Type getFieldType(Class clazz, String name) {
		Type pt;
		try {
			pt = clazz.getDeclaredField(name).getGenericType();
			return pt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Type[] getFieldGenericType(Class clazz, String name) {
		Type pt;
		try {
			pt = clazz.getDeclaredField(name).getGenericType();
			return getGenericType(pt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Type[] getGenericType(Type t) {
		try {

			ParameterizedType pt = (ParameterizedType) t;
			int length = pt.getActualTypeArguments().length;
			Type[] metas = new Type[length];
			for (int i = 0; i < metas.length; i++) {
				Type type = pt.getActualTypeArguments()[i];
				metas[i] = type;
			}

			return metas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	
	public static Class getRawClass(Type parameterizedType) {
		try {
			return Class.forName(getRawClassName(parameterizedType));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString()+parameterizedType.toString());
			return null;
		}
	}
	public static String getRawClassName(Type parameterizedType) {
		Type typeValue = parameterizedType;
		try {
			if (isGeneric(parameterizedType)) {
				ParameterizedType type = (ParameterizedType) parameterizedType;
				typeValue = type.getRawType();
				return typeValue.toString()
				.replace("interface", "").trim().replace("class", "")
				.trim();
			}else{
				return parameterizedType.toString()
						.replace("interface", "").trim().replace("class", "")
						.trim();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

