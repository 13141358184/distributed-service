package com.xiaoheiwu.service.serializer.meta.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.FieldMeta;
import com.xiaoheiwu.service.serializer.meta.IMetaParser;
import com.xiaoheiwu.service.serializer.meta.IMetaProductor;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.ObjectMeta;
import com.xiaoheiwu.service.serializer.util.MethodUtil;

public class MetaParser implements IMetaParser {
	private ConcurrentMap<String, IObjectMeta> creatingObject = new ConcurrentHashMap<String, IObjectMeta>();

	@Override
	public IObjectMeta parse(Class clazz) {
		DataType dataType = DataType.getDataType(clazz);
		IObjectMeta meta = null;
		if (dataType == DataType.OBJECT) {
			meta = parseObject(clazz);
		} else {
			IMetaProductor productor = dataType.getMetaProductor();
			meta = productor.createObjectMeta(clazz);
		}

		return meta;
	}

	private IObjectMeta parseObject(Class clazz) {
		String name = clazz.getName();
		if (creatingObject.containsKey(name))
			return creatingObject.get(name);

		ObjectMeta meta = new ObjectMeta();
		meta.setRawClassName(name);
		creatingObject.putIfAbsent(name, meta);
		List<FieldMeta> fieldMetas = createFieldMeta(clazz);
		meta.setFieldMetas(fieldMetas);
		creatingObject.remove(name);
		return meta;
	}

	protected List<FieldMeta> createFieldMeta(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<FieldMeta> list = new ArrayList<FieldMeta>();
		for (Field field : fields) {
			try {
				int modifier = field.getModifiers();
				if (Modifier.isAbstract(modifier) || Modifier.isFinal(modifier)
						|| Modifier.isNative(modifier)
						|| Modifier.isStatic(modifier)
						|| Modifier.isTransient(modifier)) {
					continue;
				}
				String fieldName=field.getName();
				String methodName = MethodUtil.getGetMethod(fieldName);
				Method getMethod = clazz.getMethod(methodName);
				Class class1 = getMethod.getReturnType();
				DataType dataType = DataType.getDataType(class1);
				String setMethodName=MethodUtil.getSetMethod(fieldName);
				Method setMethod=clazz.getMethod(setMethodName, class1);
				
				FieldMeta fieldMeta = new FieldMeta();
				fieldMeta.setFieldName(field.getName());
				fieldMeta.setGetMethod(getMethod);
				fieldMeta.setSetMethod(setMethod);
				Class fieldClass = dataType.getDataTypeClass();
				if (DataType.OBJECT == dataType) {
					Type type = GenericTypeUtil.getFieldType(clazz,
							field.getName());
					fieldClass = GenericTypeUtil.getRawClass(type);
				}
				IObjectMeta objectMeta = parse(fieldClass);
				fieldMeta.setObjectMeta(objectMeta);
				fieldMeta.setAnnotations(field.getAnnotations());
				list.add(fieldMeta);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("不支持此类型" + clazz + field.toString(),
						e);
			}

		}
		return list;
	}

	public static void main(String[] args) {

	}
}

class Person {
	String name;
	int age;
	Person person;
	List<String> list;

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

}
