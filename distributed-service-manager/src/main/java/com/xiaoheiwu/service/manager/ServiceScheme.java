package com.xiaoheiwu.service.manager;

public enum ServiceScheme {
	SELF(1);
	private int schemeType;
	private ServiceScheme(int schemeType){
		this.schemeType=schemeType;
	}
	public int getSchemeType(){
		return this.schemeType;
	}
}
