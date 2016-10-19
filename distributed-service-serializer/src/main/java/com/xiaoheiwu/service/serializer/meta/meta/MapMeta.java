package com.xiaoheiwu.service.serializer.meta.meta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;

public class MapMeta extends AbstractCollectionMeta<Map>{
//	private IObjectMeta keyParadigm;
//	private IObjectMeta valueParadigm;
	public MapMeta(){
	}
	@Override
	public Map read(DataInput dataInput) {
		Map map=new HashMap();
		int size=dataInput.readInt();
		if(size==0)return map;
		
		for(int i=0;i<size;i++){
			IObjectMeta keyParadigmTmp =readElementMeta(dataInput);
			Object key=keyParadigmTmp.read(dataInput);
			IObjectMeta valueParadigmTmp =readElementMeta(dataInput);
			Object value=valueParadigmTmp.read(dataInput);
			map.put(key, value);
		}
		return map;
	}

	@Override
	public void write(Map t, DataOutput output) {
		int size=t.size();
		output.writeInt(size);
		if(size==0)return;
		Iterator<Entry> it = t.entrySet().iterator();
		boolean first=true;
		while(it.hasNext()){
			Entry entry = it.next();
			IObjectMeta keyParadigmTmp=writeElementMeta(entry.getKey(),output);
			keyParadigmTmp.write(entry.getKey(), output);
			IObjectMeta valueParadigmTmp=writeElementMeta(entry.getValue(),output);
			valueParadigmTmp.write(entry.getValue(), output);	
		}
	}

	@Override
	public DataType getDataType() {
		return DataType.MAP;
	}

	@Override
	public String getRawClassName() {
		return Map.class.getName();
	}
	@Override
	public void writeMeta(DataOutput output) {
		output.writeByte(DataType.MAP.getTypeValue());
//		this.keyParadigm.writeMeta(output);
//		this.valueParadigm.writeMeta(output);
	}
	

}
