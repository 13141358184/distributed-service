package com.xiaoheiwu.service.serializer.meta.meta;

import java.util.ArrayList;
import java.util.List;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class ArrayMeta extends AbstractCollectionMeta<Object[]>{

	@Override
	public Object[] read(DataInput dataInput) {
		int size=dataInput.readInt();
		Object[] objects=new Object[size];
		if(size==0)return objects;
		for(int i=0;i<size;i++){
			IObjectMeta paradigmTmp=readElementMeta(dataInput);
			Object object=paradigmTmp.read(dataInput);
			objects[i]=object;
		}
		return objects;
	}

	@Override
	public void write(Object[] t, DataOutput output) {
		int size=t.length;
		output.writeInt(size);
		if(size==0)return;
		
		for(int i=0;i<size;i++){
			IObjectMeta paradigmTmp = writeElementMeta(t[i],output);
			paradigmTmp.write(t[i], output);
		}
	}

	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.ARRAY.getTypeValue());
	}

	@Override
	public DataType getDataType() {
		return DataType.ARRAY;
	}

	@Override
	public String getRawClassName() {
		return DataType.ARRAY.getDataTypeClass().getName();
	}


}
