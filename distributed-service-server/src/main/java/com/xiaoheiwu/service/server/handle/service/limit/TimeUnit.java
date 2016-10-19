package com.xiaoheiwu.service.server.handle.service.limit;

import java.util.HashMap;
import java.util.Map;

public enum TimeUnit {
	SECOND,MINITUE,HOUR;
	private static Map<String, TimeUnit> string2TimeUnit=new HashMap<String, TimeUnit>();
	static{
		string2TimeUnit.put(SECOND.name(), SECOND);
		string2TimeUnit.put(MINITUE.name(), MINITUE);
		string2TimeUnit.put(HOUR.name(), HOUR);
	}
	public static TimeUnit getTimeUnit(String name){
		return string2TimeUnit.get(name);
	}
}
