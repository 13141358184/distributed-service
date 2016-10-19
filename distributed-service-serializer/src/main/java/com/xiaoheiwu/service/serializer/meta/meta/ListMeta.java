package com.xiaoheiwu.service.serializer.meta.meta;
import java.util.ArrayList;
import java.util.List;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IMetaProductor;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.productor.BaseTypeMetaProductor;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class ListMeta extends AbstractCollectionMeta<List>  {
//	private IObjectMeta paradigm;
	public ListMeta(){
	}
	@Override
	public DataType getDataType() {
		return DataType.LIST;
	}

	@Override
	public String getRawClassName() {
		return List.class.getName();
	}

//	public IObjectMeta getParadigm() {
//		return paradigm;
//	}
//
//	public void setParadigm(IObjectMeta paradigm) {
//		this.paradigm = paradigm;
//	}

	@Override
	public List read(DataInput dataInput) {
		List list=new ArrayList();
		int size=dataInput.readInt();
		if(size==0)return list;
		
		for(int i=0;i<size;i++){
			IObjectMeta paradigmTmp=readElementMeta(dataInput);
			Object object=paradigmTmp.read(dataInput);
			list.add(object);
		}
		return list;
	}

	
	@Override
	public void write(List t, DataOutput output) {
		int size=t.size();
		output.writeInt(size);
		if(size==0)return;
		
		for(int i=0;i<size;i++){
			IObjectMeta paradigmTmp = writeElementMeta(t.get(i),output);
			paradigmTmp.write(t.get(i), output);
		}
	}
	
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.LIST.getTypeValue());
//		this.paradigm.writeMeta(output);
	}
	

}
