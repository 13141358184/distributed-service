package com.xiaoheiwu.service.serializer.meta.productor;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IMetaFactory;
import com.xiaoheiwu.service.serializer.meta.IMetaProductor;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.BooleanMeta;
import com.xiaoheiwu.service.serializer.meta.meta.ByteMeta;
import com.xiaoheiwu.service.serializer.meta.meta.DoubleMeta;
import com.xiaoheiwu.service.serializer.meta.meta.IntMeta;
import com.xiaoheiwu.service.serializer.meta.meta.LongMeta;
import com.xiaoheiwu.service.serializer.meta.meta.ReadWriteableMeta;
import com.xiaoheiwu.service.serializer.meta.meta.StringMeta;
import com.xiaoheiwu.service.serializer.serializer.readwriteable.IReadWriteable;
import com.xiaoheiwu.service.serializer.stream.DataInput;

public class BaseTypeMetaProductor implements IMetaProductor {
	private BooleanMeta booleanMeta = new BooleanMeta();
	private ByteMeta byteMeta = new ByteMeta();
	private DoubleMeta doubleMeta = new DoubleMeta();
	private IntMeta intMeta = new IntMeta();
	private LongMeta longMeta = new LongMeta();
	private StringMeta stringMeta = new StringMeta();
	private ReadWriteableMeta readWriteableMeta = new ReadWriteableMeta();

	@Override
	public IObjectMeta createObjectMeta(Class clazz) {
		DataType dataType = DataType.getDataType(clazz);
		return createObjectMeta(dataType);
	}

	protected IObjectMeta createObjectMeta(DataType dataType) {
		if (dataType == DataType.BOOLEAN) {
			return booleanMeta;
		} else if (dataType == DataType.BYTE) {
			return byteMeta;
		} else if (dataType == DataType.DOUBLE) {
			return doubleMeta;
		} else if (dataType == DataType.INT) {
			return intMeta;
		} else if (dataType == DataType.LONG) {
			return longMeta;
		} else if (dataType == DataType.READWRITEABLE) {
			return readWriteableMeta;
		} else if (dataType == DataType.STRING) {
			return stringMeta;
		}
		return null;
	}

	@Override
	public IObjectMeta createObjectMeta(DataType type, DataInput dataInput) {
		Class class1 = type.getDataTypeClass();
		if (type == DataType.OBJECT) {
			String name = dataInput.readString();
			try {
				class1 = Class.forName(name);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		IObjectMeta meta = getObjectMeta(class1);
		return meta;
	}

	protected IObjectMeta getObjectMeta(Class class1) {
		DataType dataType = DataType.getDataType(class1);
		IObjectMeta meta = dataType.getMetaProductor().createObjectMeta(class1);
		return meta;
	}

	@Override
	public DataType getDataType(Class clazz) {
		if (clazz == null)
			return null;
		if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
			return DataType.BOOLEAN;
		} else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
			return DataType.BYTE;
		} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
			return DataType.DOUBLE;
		} else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
			return DataType.INT;
		} else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
			return DataType.LONG;
		} else if (IReadWriteable.class.isAssignableFrom(clazz)) {
			return DataType.READWRITEABLE;
		} else if (clazz.equals(String.class)) {
			return DataType.STRING;
		}
		return null;
	}

}
