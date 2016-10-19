package com.xiaoheiwu.service.serializer.serializer.meta;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.serializer.AbstractSerializer;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.meta.IntMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataOutput;
import com.xiaoheiwu.service.serializer.util.ClassUtil;
@Service
@ServiceDefault(ISerializer.class)
public class MetaSerializer extends AbstractSerializer{
	public static final String PROVIDER_NAME="MetaSerializer";
	private Map<Class, IObjectMeta> metas=new HashMap<Class, IObjectMeta>();
	
	@Override
	public boolean isSerializable(Object object) {
		String name=object.getClass().getName();
		IObjectMeta meta=getObjectMeta(object.getClass());
		if(meta==null)return false;
		return true;
	}

	@Override
	public DataOutput serialize(Object object)
			throws NotSupportSerializerException {
		
		Class class1=getClass(object);
		IObjectMeta meta = getObjectMeta(class1);
		if(meta==null){
			throw new NotSupportSerializerException();
		}
		ByteDataOutput output = new ByteDataOutput();
		writeMeta(object, meta, output);
		meta.write(object, output);
		return output;
	}

	@Override
	public Class getDataTypeClass(Class clazz) {
		return ClassUtil.getClass(clazz);
	}

	protected Class getClass(Object object) {
		Class class1=object.getClass();
		return class1;
	}

	@Override
	public Object deserialize(DataInput input){
		IObjectMeta meta=null;
		try {
			meta = readMeta(input);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(meta==null){
			throw new NotSupportSerializerException();
		}
		Object object=meta.read(input);
		return object;
	}
	protected IObjectMeta readMeta(DataInput input) throws ClassNotFoundException {
		DataType type=DataType.getType(input.readByte());
		return type.getMetaProductor().createObjectMeta(type,input);
	}



	protected IObjectMeta getObjectMeta(Class class1) {
		IObjectMeta meta=metas.get(class1);
		if(meta!=null)return meta;
		DataType dataType=DataType.getDataType(class1);
		meta=dataType.getMetaProductor().createObjectMeta(class1);
		metas.put(class1, meta);
		return meta;
	}
	
	protected void writeMeta(Object object, IObjectMeta meta, DataOutput output) {
		meta.writeMeta(output);
	}
	public static void main(String[] args) {
		ISerializer serializer=new MetaSerializer();
		byte[] data=serializer.serialize("你好").getData();
		String result=(String) serializer.deserialize(new ByteDataInput(data));
		System.out.println(result);
	
}
	
}
