package com.xiaoheiwu.service.container.classloader;


public class ClassLoaderFactory {
	private static ClassLoaderFactory instance=new ClassLoaderFactory();
	private ServiceClassLoader commonClassLoader;
	private ClassLoaderFactory(){}
	public static ClassLoaderFactory getInstance(){
		return instance;
	}
	
	
	public ServiceClassLoader createCommonClassLoader(String servicesBaseDir, String commonDir){
		if(commonClassLoader!=null)return this.commonClassLoader;
		synchronized (this) {
			if(commonClassLoader!=null)return this.commonClassLoader;
			if(servicesBaseDir==null||commonDir==null)return null;
			commonClassLoader=new ServiceClassLoader(servicesBaseDir, commonDir);
			return commonClassLoader;
		}
	}
	public ServiceClassLoader createServiceClassLoader(String servicesBaseDir, String serviceDir){
		if(commonClassLoader==null)createCommonClassLoader(servicesBaseDir, "common");
		return new ServiceClassLoader(servicesBaseDir, serviceDir, commonClassLoader);
	}
}
