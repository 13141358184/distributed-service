package com.xiaoheiwu.service.common.scaner;

import java.util.List;

/**
 * 扫描器，负责扫描加载组建
 * @author Chris
 *
 */

public interface IScaner extends Runnable{

	public void addPackage(String packageName);
	
	
	public List<String> getPackages();

}
