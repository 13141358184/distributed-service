package com.xiaoheiwu.service.container.classloader;

import com.xiaoheiwu.service.common.classloader.IRepository;
import com.xiaoheiwu.service.common.classloader.impl.CommonClassLoader;
import com.xiaoheiwu.service.common.classloader.impl.Repository;
import com.xiaoheiwu.service.container.util.PathUtil;

public class ServiceClassLoader extends CommonClassLoader{
	private String servicesBaseDir;
	private String serviceDir;
	public ServiceClassLoader(String servicesBaseDir, String serviceDir) {
		this(servicesBaseDir, serviceDir, null);
	}
	public ServiceClassLoader(String servicesBaseDir, String serviceDir, ClassLoader parent) {
		super(createRepository(servicesBaseDir,serviceDir),parent==null?ServiceClassLoader.getSystemClassLoader():parent);
		this.servicesBaseDir=servicesBaseDir;
		this.serviceDir=serviceDir;
	}
	private static IRepository createRepository(String servicesBaseDir, String serviceDir) {
		IRepository repository=new Repository();
		String path=PathUtil.getPath(servicesBaseDir, serviceDir)+"*.jar";
		repository.addJarDir(path);
		return repository;
	}
	
	
	public String getServicesBaseDir() {
		return servicesBaseDir;
	}

	public String getServiceDir() {
		return serviceDir;
	}


}
