package com.xiaoheiwu.service.serializer.meta.productor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xiaoheiwu.service.serializer.datatype.DataType;
import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.meta.IMetaParser;
import com.xiaoheiwu.service.serializer.meta.IObjectMeta;
import com.xiaoheiwu.service.serializer.meta.parser.MetaParser;

public class ObjectMetaProductor  extends BaseTypeMetaProductor{
	private IMetaParser metaParser=new MetaParser();
	private ConcurrentMap<String, IObjectMeta> name2Metas=new ConcurrentHashMap<String, IObjectMeta>();
	@Override
	public IObjectMeta createObjectMeta(Class clazz){
		return getMeta(clazz);
	}
	
	protected IObjectMeta getMeta(Class clazz) {
		String name=clazz.getName();
		IObjectMeta meta= name2Metas.get(name);
		if(meta==null){
			meta=parseMeta(clazz);
			this.name2Metas.putIfAbsent(name, meta);
		}
		return meta;
	}
	protected IObjectMeta parseMeta(Class clazz) {
		IObjectMeta meta=metaParser.parse(clazz);
		if(meta==null)throw new NotSupportSerializerException();
		return meta;
	}
	
	@Override
	public DataType getDataType(Class clazz) {
		return DataType.OBJECT;
	}

}
