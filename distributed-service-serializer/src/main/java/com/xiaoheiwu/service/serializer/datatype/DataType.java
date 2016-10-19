package com.xiaoheiwu.service.serializer.datatype;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.xiaoheiwu.service.serializer.meta.IMetaProductor;
import com.xiaoheiwu.service.serializer.meta.productor.ArrayMetaProductor;
import com.xiaoheiwu.service.serializer.meta.productor.BaseTypeMetaProductor;
import com.xiaoheiwu.service.serializer.meta.productor.ListMetaProductor;
import com.xiaoheiwu.service.serializer.meta.productor.MapMetaProductor;
import com.xiaoheiwu.service.serializer.meta.productor.ObjectMetaProductor;
import com.xiaoheiwu.service.serializer.meta.productor.SetMetaProductor;
import com.xiaoheiwu.service.serializer.serializer.readwriteable.IReadWriteable;

/**
 * 
 * @author Chris
 *
 */
public enum DataType {
	INT((byte) 1, 0, new BaseTypeMetaProductor(), int.class), LONG((byte) 2, 0,
			new BaseTypeMetaProductor(), long.class), DOUBLE((byte) 3, 0,
			new BaseTypeMetaProductor(), double.class), BOOLEAN((byte) 4, 0,
			new BaseTypeMetaProductor(), boolean.class), STRING((byte) 5, 3,
			new BaseTypeMetaProductor(), String.class), LIST((byte) 6, 1,
			new ListMetaProductor(), List.class), MAP((byte) 7, 2,
			new MapMetaProductor(), Map.class), SET((byte) 8, 1,
			new SetMetaProductor(), Set.class), BYTE((byte) 9, 0,
			new BaseTypeMetaProductor(), byte.class), OBJECT((byte) 10, 6,
			new ObjectMetaProductor(), Object.class), READWRITEABLE((byte) 11,
			4, new BaseTypeMetaProductor(), IReadWriteable.class)
			,ARRAY((byte)12,1,new ArrayMetaProductor(),Object[].class);
	private byte value;// 对应的byte表示
	private int type;// 0=primitive;1=collection;2:map;3:string;4:READWRITEABLE;6:OBJECT;
	private IMetaProductor productor;
	private Class clazz;
	private static Map<Byte, DataType> types = new HashMap<Byte, DataType>();

	private DataType(byte value, int type, IMetaProductor productor, Class clazz) {
		this.value = value;
		this.type = type;
		this.productor = productor;
		this.clazz = clazz;
	}

	static {
		addType(INT.value, INT, int.class, Integer.class);
		addType(BYTE.value, BYTE, byte.class, Byte.class);
		addType(BOOLEAN.value, BOOLEAN, boolean.class, Boolean.class);
		addType(BOOLEAN.value, INT, int.class, Integer.class);
		addType(LONG.value, LONG, long.class, Long.class);
		addType(DOUBLE.value, DOUBLE, double.class, Double.class);
		addType(STRING.value, STRING, String.class);
		addType(LIST.value, LIST, List.class);
		addType(SET.value, SET, Set.class);
		addType(MAP.value, MAP, Map.class);
		addType(OBJECT.value, OBJECT, Object.class);
		addType(READWRITEABLE.value, READWRITEABLE, IReadWriteable.class);
		addType(ARRAY.value, ARRAY, ARRAY.getDataTypeClass());
	}

	public IMetaProductor getMetaProductor() {
		return this.productor;
	}

	public Class getDataTypeClass() {
		return this.clazz;
	}

	public byte getTypeValue() {
		return value;
	}

	public static DataType getDataType(String className) {
		Class clazz;
		try {
			clazz = Class.forName(className);
			return getDataType(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static DataType getDataType(Class clazz) {
		Iterator<DataType> it = types.values().iterator();
		DataType result = null;
		while (it.hasNext()) {
			DataType type = it.next();
			if (type == DataType.OBJECT)
				continue;
			result = type.getMetaProductor().getDataType(clazz);
			if (result != null)
				return result;
		}
		return DataType.OBJECT.getMetaProductor().getDataType(clazz);
	}

	public static DataType getType(byte key) {
		return types.get(key);
	}

	private static void addType(byte id, DataType type, Class selfClass,
			Class packageClass) {
		types.put(id, type);
	}

	private static void addType(byte id, DataType type, Class selfClass) {
		addType(id, type, selfClass, null);
		
		
	}
	public static void main(String[] args) {
		String[] d=new String[4];
		System.out.println(d.getClass());
	}

}
