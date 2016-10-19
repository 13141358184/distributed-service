package com.xiaoheiwu.service.serializer.meta.header;

import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoheiwu.service.serializer.meta.FieldMeta;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.ObjectMeta;

public class FieldNullBitSet extends BitSet {
	private Map<String, Integer> field2Index = new HashMap<>();
	private Map<Integer, FieldMeta> index2FieldMeta = new HashMap<>();
	private ObjectMeta objectMeta;

	public FieldNullBitSet(byte[] values, ObjectMeta objectMeta) {
		this.objectMeta = objectMeta;
		initField2Index();
		for (int i = 0; i < values.length; i++) {
			if (values[i] == 0) {
				FieldMeta fieldMeta = index2FieldMeta.get(i);
				if (fieldMeta.isRequired())
					throw new RuntimeException("字段标记为不能为空" + fieldMeta);
				this.set(i);

			}
		}
	}

	public FieldNullBitSet(ObjectMeta objectMeta) {
		this.objectMeta = objectMeta;
		initField2Index();
	}

	private void initField2Index() {
		List<FieldMeta> metas = objectMeta.getFieldMetas();
		for (int i = 0; i < metas.size(); i++) {
			field2Index.put(metas.get(i).getFieldName(), i);
			index2FieldMeta.put(i, metas.get(i));
		}
	}

	public byte[] getBytes() {
		byte[] bytes = new byte[objectMeta.getFieldMetas().size()];
		for (int i = 0; i < bytes.length; i++) {
			boolean isNull = this.get(i);
			if (isNull)
				bytes[i] = 0;
			else
				bytes[i] = 1;

		}
		return bytes;
	}

	public boolean isNull(String name) {
		int index = field2Index.get(name);
		return this.get(index);
	}

	public byte[] getBytes(Object object) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		List<FieldMeta> metas = objectMeta.getFieldMetas();
		int size = metas.size();
		byte[] bytes = new byte[size];
		for (int i = 0; i < size; i++) {
			FieldMeta fieldMeta = metas.get(i);
			Object value = fieldMeta.getFieldValue(object);
			if (value == null) {
				if (fieldMeta.isRequired())
					throw new RuntimeException("字段标记为不能为空" + fieldMeta);
				bytes[i] = 0;
				this.set(i);
			} else {
				bytes[i] = 1;
			}
		}
		return bytes;
	}

}
