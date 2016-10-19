package com.xiaoheiwu.service.serializer.meta.header;

import java.lang.reflect.InvocationTargetException;



import java.util.HashMap;
import java.util.Map;

import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.exception.ObjectMetaVersionException;
import com.xiaoheiwu.service.serializer.meta.meta.ObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class ObjectHeaderService {
	private final static ObjectHeaderService instance=new ObjectHeaderService();
	private byte version=0;//对象的版本，字段名称排序后，根据一定算法算出。当老版本对象对应新的meta时，通过此字段做校验
	private byte magicNum=(byte)11001010;//魔数，区分序列化工具
	private ObjectHeaderService(){}
	public static ObjectHeaderService getInstance(){
		return instance;
	}
	public ObjectHeader readObjectHeader(DataInput dataInput, ObjectMeta objectMeta) {
		boolean success=readAndVerifyMagic(dataInput);//读取魔数并进行校验
		if(!success)throw new NotSupportSerializerException();
		success=readAndVerifyVersion(dataInput);//读取版本并进行校验
		if(!success)throw new ObjectMetaVersionException();
		ObjectHeader header=new ObjectHeader(objectMeta);
		header.readObjectHeader(dataInput);
		return header;
	}
	public ObjectHeader writeObjectHeader(Object object,DataOutput dataOutput,ObjectMeta objectMeta) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		dataOutput.writeByte(magicNum);
		dataOutput.writeInt(version);
		ObjectHeader header=new ObjectHeader(objectMeta);
		header.writeObjectHeader(object, dataOutput);
		return header;
	}
	
	
	private boolean readAndVerifyVersion(DataInput dataInput) {
		int readVersion=dataInput.readInt();
		if(readVersion==version)return true;
		return false;
	}
	private boolean readAndVerifyMagic(DataInput dataInput) {
		byte readMagic=dataInput.readByte();
		if(readMagic==magicNum)return true;
		return false;
	}

}
