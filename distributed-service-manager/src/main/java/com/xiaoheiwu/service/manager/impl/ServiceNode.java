package com.xiaoheiwu.service.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;




import java.util.Map;
import java.util.Map.Entry;

import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.common.configure.impl.Configure;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;

public class ServiceNode  implements IServiceNode{
	private String ip;
	private int port;
	private Map<String, String> strategy=new HashMap<String, String>();
	private IService service;
	public ServiceNode(String ip, int port){
		this.ip=ip;
		this.port=port;
	}
	public ServiceNode(String indentity) {
		if(indentity==null||"".equals(indentity))return;
		String[]  values=indentity.split(":");
		if(values.length!=2)return;
		this.ip=values[0].trim();
		this.port=Integer.valueOf(values[1].trim());
	}
	public static List<IServiceNode> convert(List<String> children){
		List<IServiceNode> nodes=new ArrayList<IServiceNode>();
		for(String child:children){
			IServiceNode serverNode=new ServiceNode(child);
			nodes.add(serverNode);
		}
		return nodes;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getIdentity() {
		return ip+":"+port;
	}
	@Override
	public int hashCode() {
		return getIdentity().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		ServiceNode node=(ServiceNode)obj;
		return this.getIdentity().equals(node.getIdentity());
	}
	public int getStatus() {
		String value=strategy.get(ServiceConfigureKey.STATUS.name());
		if(value==null)return IService.ONLINE;
		try{
			return Integer.valueOf(value);
		}catch(Exception e){
			return IService.OFFLINE;
		}
	}
	public void setStatus(int status) {
		this.strategy.put(ServiceConfigureKey.STATUS.name(), String.valueOf(status));
	}
	@Override
	public IService getService() {
		return this.service;
	}

	public Map<String, String> getStrategy() {
		return strategy;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setStrategy(Map<String, String> strategy) {
		this.strategy = strategy;
	}
	public void setService(IService service) {
		this.service = service;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("dentity:"+getIdentity()+"\r\n");
		sb.append("strategy:"+this.strategy.size()+"\r\n");
		Iterator<Entry<String, String>> it = strategy.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry=it.next();
			sb.append("    key:"+entry.getKey()+";value:"+entry.getValue()+"\r\n");
		}
		return sb.toString();
	}
	@Override
	public String getConfigure(String key) {
		if(key==null)return null;
		if(getStrategy()!=null&&getStrategy().containsKey(key)){
			return getStrategy().get(key);
		}
		if(service!=null&&service.getStrategy()!=null){
			return service.getStrategy().get(key);
		}
		return null;
	}
	@Override
	public IConfigure getConfigure() {
		IConfigure configure=new Configure();
		if(service!=null&&service.getStrategy()!=null)configure.load(this.service.getStrategy());
		configure.load(this.getStrategy());
		return configure;
	}
	
	
}
