package com.xiaoheiwu.service.serializer.meta.meta;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class SetMeta extends AbstractCollectionMeta<Set>{
//	private IObjectMeta paradigm;
	public SetMeta(){
	}
	@Override
	public Set read(DataInput dataInput) {
		Set set=new HashSet();
		int size=dataInput.readInt();
		if(size==0)return set;
		for(int i=0;i<size;i++){
			IObjectMeta paradigm = this.readElementMeta(dataInput);
			Object object=paradigm.read(dataInput);
			set.add(object);
		}
		return set;
	}

	@Override
	public void write(Set t, DataOutput output) {
		int size=t.size();
		output.writeInt(size);
		if(size==0)return;
		Iterator it = t.iterator();
		while(it.hasNext()){
			Object value=it.next();
			IObjectMeta paradigm =this.writeElementMeta(value, output);
			paradigm.write(value, output);
		}
	}

	@Override
	public DataType getDataType() {
		return DataType.SET;
	}

	@Override
	public String getRawClassName() {
		return Set.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.SET.getTypeValue());
//		this.paradigm.writeMeta(output);
	}

}
