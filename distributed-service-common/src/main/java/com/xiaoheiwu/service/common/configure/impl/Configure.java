package com.xiaoheiwu.service.common.configure.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.common.log.Log;

public class Configure extends HashMap<String, Object> implements IConfigure{
	private Log logger=Log.getLogger(this.getClass());
	@Override
	public Integer getIntValue(String key) {
		Object object=getObjectValue(key);
		if(Integer.class.isInstance(object)){
			return (Integer)object;
		}else if(String.class.isInstance(object)){
			return Integer.valueOf((String)object);
		}
		return null;
	}

	@Override
	public Long getLongValue(String key) {
		Object object=getObjectValue(key);
		if(Long.class.isInstance(object)){
			return (Long)object;
		}else if(String.class.isInstance(object)){
			return Long.valueOf((String)object);
		}
		return null;
	}
	@Override
	public <T> T getObjectValue(String key){
		Object object=this.getValue(key);
		if(object!=null){
			try{
				return (T) object;
			}catch(ClassCastException e){
				e.printStackTrace();
				return null;
			}
		
		}
		return null;
	}
	@Override
	public Double getDoubleValue(String key) {
		Object object=getObjectValue(key);
		if(Double.class.isInstance(object)){
			return (Double)object;
		}else if(String.class.isInstance(object)){
			return Double.valueOf((String)object);
		}
		return null;
	}

	@Override
	public String getStringValue(String key) {
		return this.<String>getObjectValue(key);
	}

	@Override
	public Boolean getBooleanValue(String key) {
		Object object=getObjectValue(key);
		if(Boolean.class.isInstance(object)){
			return (Boolean)object;
		}else if(String.class.isInstance(object)){
			return Boolean.valueOf((String)object);
		}
		return null;
	}

	@Override
	public Byte getByteValue(String key) {
		Object object=getObjectValue(key);
		if(Byte.class.isInstance(object)){
			return (Byte)object;
		}else if(String.class.isInstance(object)){
			return Byte.valueOf((String)object);
		}
		return null;
	}

	@Override
	public <T> T[] getArrayValue(String key, Class<T> generic) {
		return this.<T[]>getObjectValue(key);
	}

	@Override
	public <T> List<T> getListValue(String key, Class<T> generic) {
		return this.<List<T>>getObjectValue(key);
	}

	@Override
	public <T> void putValue(String key, T value) {
		this.put(key, value);
	}

	protected Object getValue(String key){
		return this.get(key);
	}

	@Override
	public void putProperties(Properties properties) {
		Iterator<java.util.Map.Entry<Object, Object>> it = properties.entrySet().iterator();
		while(it.hasNext()){
			java.util.Map.Entry<Object, Object> entry=it.next();
			if(entry==null||entry.getKey()==null||entry.getValue()==null){
				logger.error("有一条数据不能加载，原因是有数据为null."+" key = "+entry.getKey()+"; value= "+entry.getValue());
				continue;
			}
			String key=(String)entry.getKey();
			String value=(String)entry.getValue();
			this.putValue(key, value);
		}
	}
	@Override
	public void load(Map map){
		if(map==null)return;
		this.putAll(map);
	}
	@Override
	public void load(InputStream in) throws IOException {
		Properties p=new Properties();
		p.load(in);
		this.putProperties(p);
		
	}
	public <T> void putMap(Map<String, T> map){
		Iterator<java.util.Map.Entry<String, T>> it = map.entrySet().iterator();
		while(it.hasNext()){
			java.util.Map.Entry<String, T> entry=it.next();
			String key=entry.getKey();
			T t=entry.getValue();
			this.putValue(key, t);
		}
	}

	@Override
	public void load(String fileName) throws IOException {
		InputStream in= new FileInputStream(fileName);
		this.load(in);
	}

	@Override
	public Map<String, Object> getMapValue() {
		return this;
	}
}
