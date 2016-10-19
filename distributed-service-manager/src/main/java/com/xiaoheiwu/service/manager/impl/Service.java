package com.xiaoheiwu.service.manager.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.zookeeper.util.PathUtil;

public class Service implements IService{
	private static final String ENABLE_PATH="enable";
	private static final String DISABLE_PATH="disable";
	private static final String ALARM_PATH="alarm";
	private String name;
	private String version="1.0";
	private Map<String, String> strategy=new HashMap<String, String>();
	
	public Service(String name){
		this.setName(name);
	}
	
	public int getStatus() {
		String value=strategy.get(ServiceConfigureKey.STATUS.name());
		if(value==null)return ONLINE;
		try{
			return Integer.valueOf(value);
		}catch(Exception e){
			return OFFLINE;
		}
	}
	public void setStatus(int status) {
		this.strategy.put(ServiceConfigureKey.STATUS.name(), String.valueOf(status));
	}
	public Service(){}
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static String getEnablePath(String name) {
		return "/"+name+"/"+ENABLE_PATH;
	}
	public static String getDisablePath(String name) {
		return "/"+name+"/"+DISABLE_PATH;
	}
	public static String getAlarmInfoPath(String name) {
		return "/"+name+"/"+ALARM_PATH;
	}
	public static String getServicePath(String name){
		return "/"+name;
	}
	public static String getServiceNodePath(String name, String identity, boolean enable){
		if(!enable)return PathUtil.getServiceSubNodePath(getDisablePath(name), identity);
		return PathUtil.getServiceSubNodePath(getEnablePath(name), identity);
	}
	
	public static String parseServiceName(String path){
		String[] values=path.split("/");
		if(values.length>1)return values[1];
		return null;
	}
	public static String parseServiceNodeIndentity(String path){
		if(!isChildrenPath(path))return null;
		String[] values=path.split("/");
		return values[3];
	}
	public static boolean isChildrenPath(String path){
		String[] values=path.split("/");
		if(values.length==4&&(values[2].equals(ENABLE_PATH)||values[2].equals(DISABLE_PATH))){
			return true;
		}
		return false;
	}
	@Override
	public Map<String, String> getStrategy() {
		
		return this.strategy;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setStrategy(Map<String, String> strategy) {
		this.strategy = strategy;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("name:"+name+"\r\n");
		sb.append("version:"+version+"\r\n");
		sb.append("strategy:"+this.strategy.size()+"\r\n");
		Iterator<Entry<String, String>> it = strategy.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry=it.next();
			sb.append("    key:"+entry.getKey()+";value:"+entry.getValue()+"\r\n");
		}
		return sb.toString();
	}
public static void main(String[] args) {
	String path="/HelloAPI/enable/192.168.1.106:8087";
	System.out.println(isChildrenPath(path));
	System.out.println(parseServiceName(path));
	System.out.println(parseServiceNodeIndentity(path));
}
}
