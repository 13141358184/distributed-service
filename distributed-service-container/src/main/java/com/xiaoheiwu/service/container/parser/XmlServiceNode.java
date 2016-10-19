package com.xiaoheiwu.service.container.parser;

import java.util.HashMap;
import java.util.Map;

public class XmlServiceNode {
	private String ip;
	private String port;
	private Map<String, String >strategys =new HashMap<String, String>();
	public String getIp() {
		return ip;
	}

	public Map<String, String> getStrategys() {
		return strategys;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setStrategys(Map<String, String> strategys) {
		this.strategys = strategys;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}