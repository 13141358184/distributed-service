package com.xiaoheiwu.service.common.configure.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.common.util.ClassFinderUtil;
/**
 * 根据包名自动扫描当前包和子包的所有属性文件并加载。相同的键会被覆盖
 * @author Chris
 *
 */
public class AutoPropertiesLoaderConfigure extends Configure{
	private Log logger=Log.getLogger(this.getClass());
	public AutoPropertiesLoaderConfigure(String packageName){
		List<Properties> propertieses=findPropertiesFile(packageName);
		if(propertieses==null||propertieses.size()==0){
			logger.error("没有装载任何属性文件");
			return;
		}
		loadProperties(propertieses);
	}
	
	public AutoPropertiesLoaderConfigure(String... propertiesFileNames){
		loadProperties(propertiesFileNames);
	}

	
	protected boolean loadProperties(String[] propertiesFileNames) {
		for(String propertiesFileName: propertiesFileNames){
			InputStream in;
			try {
				in = new FileInputStream(propertiesFileName);
				this.load(in);
			} catch (Exception e) {
				logger.error("装载文件出错",e);
				return false;
			}
			
		}
		return true;
	}
	protected void loadProperties(List<Properties> propertieses) {
		for(Properties properties: propertieses){
			this.putProperties(properties);
		}
		
	}
	
	private List<Properties> findPropertiesFile(String packageName) {
		List<Properties> propertieses=new ArrayList<Properties>();
		try {
			propertieses = ClassFinderUtil.getProperties(packageName);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return propertieses;
	}
}
