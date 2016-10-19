package com.xiaoheiwu.service.transport.transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xiaoheiwu.service.transport.ITransportArgs;

public class TransportArgs implements ITransportArgs{
	protected int type;
	protected final Map<String, Object> args;
	public TransportArgs(int type, Map<String, Object> args){
		this.type=type;
		this.args=args;
	}
	@Override
	public int getTransportType() {
		return type;
	}

	@Override
	public String getIdentity() {
		if(args==null||args.size()==0)return null;
		StringBuilder sb=new StringBuilder(type+":");
		Set<String> keySet=args.keySet();
		List<String> keyList=convertListAndSort(keySet);
		for(String key: keyList){
			Object value=args.get(key);
			if(value==null)throw new RuntimeException("value 不能为空, key is "+key);
			sb.append(key+":"+value);
		}
		return sb.toString();
	}

	
	@Override
	public Map<String, Object> getArgs() {
		return args;
	}
	private List<String> convertListAndSort(Set<String> keySet) {
		List<String> keyList=new ArrayList<String>();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()){
			String key=it.next();
			keyList.add(key);
		}
		Collections.sort(keyList);
		return keyList;
	}
}
