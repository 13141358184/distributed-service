package com.xiaoheiwu.service.serializer.serializer.readwriteable;

import com.xiaoheiwu.service.serializer.AbstractSerializer;
import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataOutput;
import com.xiaoheiwu.service.serializer.util.ClassUtil;


public class ReadWriteableSerializer extends AbstractSerializer{

	@Override
	public boolean isSerializable(Object object) {
		if(IReadWriteable.class.isInstance(object))return true;	
		return false;
	}

	@Override
	public DataOutput serialize(Object object) throws NotSupportSerializerException {
		if(!isSerializable(object))throw new NotSupportSerializerException();
		IReadWriteable readWriteable=(IReadWriteable) object;
		DataOutput output=new ByteDataOutput();
		output.writeString(object.getClass().getName());
		readWriteable.write(output);
		return output;
	}

	@Override
	public Object deserialize(DataInput dataInput) throws NotSupportSerializerException {
		try{
			String className=dataInput.readString();
			Class class1=Class.forName(className);
			DataType type=DataType.getDataType(class1);
			if(DataType.READWRITEABLE!=type){
				throw new RuntimeException("解析错误，class 不是readable的。"+class1.getName());
			}
			IReadWriteable readable=(IReadWriteable)class1.newInstance();
			readable.read(dataInput);
			return readable;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Class getDataTypeClass(Class clazz) {
		return ClassUtil.getClass(clazz);
	}

}
