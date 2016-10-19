package com.xiaoheiwu.service.common.classloader;

import java.net.URL;
import java.util.List;

public interface IRepository {

	/**
	 * 增加jar，
	 * @param jarName jar的全路径
	 */
	public void addJar(String... jarName);
	
	/**
	 * 增加一个目录下所有的jar
	 * @param dirName 目录名称
	 */
	public void addJarDir(String... dirName);
	
	/**
	 * 增加一个文件或目录
	 * @param fileName 文件或目录
	 */
	public void addFile(String... fileName);
	
	/**
	 * 通过；来分割路径，路径可以是jar，*.jar和常规目录
	 * @param path
	 */
	public void addPath(String path);
	
	/**
	 * 
	 * @return 获取所欲的资源
	 */
	public List<URL> getRepository();
}
