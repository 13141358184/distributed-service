package com.xiaoheiwu.service.common.configure;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * 通用的配置接口。可以加载属性文件（通过inputstream获取）。提供一些读取数据的方法
 * @author Chris
 *
 */
public interface IConfigure {
	public Integer getIntValue(String key);
	public Long getLongValue(String key);
	public Double getDoubleValue(String key);
	public String getStringValue(String key);
	public Boolean getBooleanValue(String key);
	public Byte getByteValue(String key);
	public <T> T getObjectValue(String key); 
	public <T> T[] getArrayValue(String key, Class<T> generic);
	public <T> List<T> getListValue(String key, Class<T> generic);
	
	public Map<String, Object> getMapValue();
	
	public <T> void putValue(String key, T t);
	
	public void putProperties(Properties properties);
	
	public void load(InputStream in)throws IOException ;
	
	public void load(String fileName)throws IOException ;
	
	public void load(Map map);
	
	
	
}
