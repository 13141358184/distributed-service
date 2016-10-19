package com.xiaoheiwu.service.container.parser;

import java.util.HashMap;
import java.util.Map;

public class XmlService {
	private String serviceName;
	private String version;
	private String interfaceClassName;
	private String implClassName;
	private Map<String, String> strategys=new HashMap<String, String>();
	private XmlServiceNode serviceNode;
	public String getServiceName() {
		return serviceName;
	}
	public String getVersion() {
		return version;
	}
	public String getInterfaceClassName() {
		return interfaceClassName;
	}
	public String getImplClassName() {
		return implClassName;
	}
	public Map<String, String> getStrategys() {
		return strategys;
	}
	public XmlServiceNode getServiceNode() {
		return serviceNode;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setInterfaceClassName(String interfaceClassName) {
		this.interfaceClassName = interfaceClassName;
	}
	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}
	public void setStrategys(Map<String, String> strategys) {
		this.strategys = strategys;
	}
	public void setServiceNode(XmlServiceNode serviceNode) {
		this.serviceNode = serviceNode;
	}
	
}
