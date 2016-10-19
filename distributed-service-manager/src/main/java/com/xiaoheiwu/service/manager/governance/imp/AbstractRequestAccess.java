package com.xiaoheiwu.service.manager.governance.imp;

import com.xiaoheiwu.service.manager.governance.IRequestAccess;

public abstract class AbstractRequestAccess<REQUEST> implements IRequestAccess<String, REQUEST>{
	protected String value;
	@Override
	public boolean setValue(String t) {
		this.value=t;
		return true;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	protected Integer convertInterger(){
		try{
			Integer i=Integer.valueOf(value);
			return i;
		}catch(Exception e){
			return null;
		}
	}


}
