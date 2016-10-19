package com.xiaoheiwu.service.serializer.meta.meta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;



import java.util.Map;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.FieldMeta;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.header.ObjectHeader;
import com.xiaoheiwu.service.serializer.meta.header.ObjectHeaderService;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
public class ObjectMeta implements IObjectMeta<Object>{
	private List<FieldMeta> fieldMetas=new ArrayList<FieldMeta>();
	private ObjectHeaderService headerService=ObjectHeaderService.getInstance();
	private String rawClassName=null;
	@Override
	public Object read(DataInput dataInput) {
		Object object=null;
		try{
			Class class1=Class.forName(rawClassName);
			object=class1.newInstance();
			ObjectHeader header= headerService.readObjectHeader(dataInput,this);
			setField2Object(dataInput, header, object);
			return object;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 从输入流读出各个字段的数据，并设置到对应的字段中去
	 * @param dataInput 输入流
	 * @param header 对象头
	 * @param object 要反序列化的对象实例
	 * @throws SecurityException 
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setField2Object(DataInput dataInput, ObjectHeader header,
			Object object) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for(FieldMeta fieldMeta: this.fieldMetas){
			boolean ignore=needIgnore(fieldMeta,header);//是否字段需要忽略，如字段值为null或有忽略标签
			if(ignore)continue;
//			if(fieldMeta.getObjectMeta().getRawClassName().equals(Object.class.getName())){
//				value= serializer.deserialize(dataInput);
//			}else{
//				value=fieldMeta.getObjectMeta().read(dataInput);
//			}
			Object value=fieldMeta.getObjectMeta().read(dataInput);
			fieldMeta.setFieldValue(object, value);
//			MethodUtil.setValue2Object(fieldMeta.getFieldName(),fieldMeta.getObjectMeta().getDataType().getDataTypeClass(),value,object);
		}
		
	}

	



	@Override
	public void write(Object t, DataOutput output) {
		try{
			ObjectHeader header=headerService.writeObjectHeader(t,output,this);
			writeFieldValue(t,header,output);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	private void writeFieldValue(Object object, ObjectHeader header, DataOutput output) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for(FieldMeta fieldMeta: this.fieldMetas){
			boolean ignore=needIgnore(fieldMeta, header);
			if(ignore)continue;
			Object value=fieldMeta.getFieldValue(object);
			fieldMeta.getObjectMeta().write(value, output);
//			if(fieldMeta.getObjectMeta().getRawClassName().equals(Object.class.getName())){
//				DataOutput out=serializer.serialize(value);
//				output.writeDataOutput(out);
//			}else{
//				fieldMeta.getObjectMeta().write(value, output);
//			}
		}
		
	}

	
	/**
	 * 对于某些字段不需要处理
	 * @param fieldMeta 字段meta
	 * @param header header
	 * @return
	 */
	protected  boolean needIgnore(FieldMeta fieldMeta, ObjectHeader header) {
		String name=fieldMeta.getFieldName();
		if(isNull(name,header))return true;
		if(fieldMeta.isAnnotationIgnore())return true;
		return false;
	}

	
	/**
	 * 通过header得知某些字段值不为空，在处理的时候自动跳过这些字段
	 * @param name 字段名称
	 * @param header header
	 * @return
	 */
	private boolean isNull(String name, ObjectHeader header) {
		return header.isNull(name);
	}

	@Override
	public DataType getDataType() {
		return DataType.OBJECT;
	}

	@Override
	public String getRawClassName() {
		return rawClassName;
	}

	public List<FieldMeta> getFieldMetas() {
		return fieldMetas;
	}
	
	/**
	 * 由于jdk的版本问题可能导致反射的字段顺序不一样。在这里会对字段进行排序。确保序列化的一致性
	 * @param fieldMetas
	 */
	public void setFieldMetas(List<FieldMeta> fieldMetas) {
		this.fieldMetas = fieldMetas;
		Collections.sort(this.fieldMetas, new Comparator<FieldMeta>() {

			@Override
			public int compare(FieldMeta o1, FieldMeta o2) {
				return o1.getFieldName().compareTo(o2.getFieldName());
			}
		});
	}

	public void setRawClassName(String rawClassName) {
		this.rawClassName = rawClassName;
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.OBJECT.getTypeValue());
		output.writeString(rawClassName);
	}

	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("class "+this.rawClassName+"{\r\n");
		String blank="	";
		for(FieldMeta fieldMeta: this.fieldMetas){
			String className=fieldMeta.getObjectMeta().getRawClassName();
			sb.append(blank+className+" "+fieldMeta.getFieldName()+"\r\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
}
