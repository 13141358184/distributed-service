package com.xiaoheiwu.service.serializer.meta.header;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


import com.xiaoheiwu.service.serializer.meta.meta.ObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class ObjectHeader {
	private ObjectMeta objectMeta;//head对应的源对象
	private FieldNullBitSet nullFieldBit;//对应每个字段是否为空
	private boolean useCompress=false;//如果启用压缩，后面存放的是一个byte的索引，通过头部获取ObjectMeta
	private Map<Byte, ObjectMeta> index2CompressedObjectMeta=new HashMap<Byte, ObjectMeta>();//对于启用压缩后，压缩的对象写在头部
	public ObjectHeader(ObjectMeta objectMeta){
		this.objectMeta=objectMeta;
		nullFieldBit=new FieldNullBitSet(objectMeta);//对应每个字段是否为空
	}
	public void readObjectHeader(DataInput dataInput) {
		nullFieldBit =readNullFieldBit(dataInput);//读取字段是否为空设置
		useCompress=readCompressFlag(dataInput);//读取是否压缩
	}
	public void writeObjectHeader(Object object,DataOutput dataOutput) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		byte[] bytes=nullFieldBit.getBytes(object);
		byte length=(byte)bytes.length;
		dataOutput.writeByte(length);
		if(bytes.length>0)dataOutput.writeBytes(bytes);
		dataOutput.writeBoolean(useCompress);
	}
	public boolean isNull(String name){
		return nullFieldBit.isNull(name);
	}


	private boolean readCompressFlag(DataInput dataInput) {
		return dataInput.readBoolean();
	}
	private FieldNullBitSet readNullFieldBit(DataInput dataInput) {
		int byteCount=dataInput.readByte();
		if(byteCount==0)return new FieldNullBitSet(new byte[0],this.objectMeta);
		byte[] bytes=dataInput.readBytes(byteCount);
		return new FieldNullBitSet(bytes,this.objectMeta);
	}

	
	
}
