package com.xiaoheiwu.service.common.component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.xiaoheiwu.service.common.log.Log;
/**
 * 通过指定的属性文件加载Provider。key=接口名称;value=privder 全路径名称
 * @author Chris
 *
 */
public class ProviderLoaderByProperties {
	private Log logger=Log.getLogger(this.getClass());
	private List<String> insertOrder=new ArrayList<String>();
	private Map<String, String> properties=new HashMap<String, String>();
	
	public Iterator<Entry<String, String>> iterator(){
		return new Iterator<Map.Entry<String,String>>() {
			int index=0;
			@Override
			public boolean hasNext() {
				return index<properties.size();
			}

			@Override
			public Entry<String, String> next() {
				String tmpKey=insertOrder.get(index);
				index++;
				while(!properties.containsKey(tmpKey)||index>=insertOrder.size()){
					insertOrder.remove(index);
					tmpKey=insertOrder.get(index);
				}
				final String key=tmpKey;
				final String value=properties.get(key);
				
				Entry<String, String> entry=new Entry<String, String>() {
					
					@Override
					public String setValue(String value) {
						return null;
					}
					
					@Override
					public String getValue() {
						return value;
					}
					
					@Override
					public String getKey() {
						return key;
					}
				};
				return entry;
			}

			@Override
			public void remove() {
				final String key=insertOrder.get(index);
				properties.remove(key);
			}
		};
	}
	
	
	public void load(String fileName){
		try {
			BufferedReader br=new BufferedReader(new FileReader(fileName));
			String line=br.readLine();
			while(line!=null){
				String[] values=line.split("=");
				if(values.length!=2){
					logger.error(line+"不符合解析规范，长度应该为2");
					continue;
				}
				if(values[0]==null||values[1]==null){
					logger.error(line+"不符合解析规范，不应该有null值");
					continue;
				}
				insertOrder.add(values[0].trim());
				properties.put(values[0].trim(), values[1].trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
