package com.xiaoheiwu.service.container.container.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import com.xiaoheiwu.service.common.callchain.CallChain;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.common.configure.impl.Configure;
import com.xiaoheiwu.service.common.executor.MutilExecutors;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.container.IContainerConfigure;
import com.xiaoheiwu.service.container.classloader.ClassLoaderFactory;
import com.xiaoheiwu.service.container.classloader.ServiceClassLoader;
import com.xiaoheiwu.service.container.container.IServerContainer;
import com.xiaoheiwu.service.server.invoker.Invoker;
import com.xiaoheiwu.service.server.service.IServiceRegistion;
import com.xiaoheiwu.service.server.service.impl.CommandService;
import com.xiaoheiwu.service.server.startup.Bootstrap;

public class ServerContainer implements IServerContainer{
	private Log logger=Log.getLogger(this.getClass());
	public static final String PORT_CONGIGURE_KEY="port";
	public static final String COMMON_JAR_DIR_CONGIGURE_KEY="commonJarDir";
	private static final int DEFAULT_PORT=8087;
	private Bootstrap bootstrap=new Bootstrap();
	private IServiceRegistion serviceRegistion=ComponentProvider.getInstance(IServiceRegistion.class);
	private int port=8087;
	private String servicesDir;
	private String commonJarDir="common";
	private IConfigure configure;
	private ServiceClassLoader commonClassdLoader;
	private ExecutorService executorService;
	public ServerContainer(String servicesDir){
		this.servicesDir=servicesDir;
		configure=loadConfigure();
		this.port=loadPort(configure);
		this.commonJarDir=loadCommonJarDir(configure);
		this.commonClassdLoader=createCommonClassLoader();
		Integer serviceThreadCount=configure.getIntValue(IContainerConfigure.SERVICE_THREAD_COUNT);
		if(serviceThreadCount!=null){
			executorService=MutilExecutors.newFixedThreadPool(serviceThreadCount);
			configure.putValue(IContainerConfigure.SERVICE_THREAD_SERVICE, executorService);
		}
	}
	
	@Override
	public boolean start() {
		this.serviceRegistion.registe("CommandService", new Invoker(new CommandService(), this.executorService, this.configure));
		startServices();
		bootstrap.bind(port);
		bootstrap.start();
		return true;
	}
	
	private IConfigure loadConfigure() {
		configure=new Configure();
		try {
			configure.load(this.servicesDir+"services.properties");
		} catch (IOException e) {
			logger.error("ServerContainer装载属性文件失败",e);
		}
		return configure;
	}
	private int loadPort(IConfigure configure2) {
		String portString=configure.getStringValue(PORT_CONGIGURE_KEY);
		if(portString==null)return DEFAULT_PORT;
		return Integer.valueOf(portString);
	}
	
	private String loadCommonJarDir(IConfigure configure) {
		String jarDir=configure.getStringValue(COMMON_JAR_DIR_CONGIGURE_KEY);
		if(jarDir!=null){
			this.commonJarDir=jarDir;
		}
		
		return commonJarDir;
	}
	private ServiceClassLoader createCommonClassLoader() {
		this.commonClassdLoader=commonClassdLoader=ClassLoaderFactory.getInstance().createCommonClassLoader(this.servicesDir, commonJarDir);;
		return commonClassdLoader;
	}
	private void startServices() {
		File file=new File(this.servicesDir);
		File[] files=file.listFiles();
		for(File service:files){
			if(service.isFile())continue;
			if(service.getName().equals(commonJarDir))continue;
			new ServiceContainer(servicesDir, service.getName(), this.port, this.configure).start();
		}
	}
	@Override
	public boolean stop() {
		return true;
	}
	

	public void setServiceRegistion(IServiceRegistion serviceRegistion) {
		this.serviceRegistion = serviceRegistion;
	}

	@Autowired
	private CallChain callChain;
	public static void main(String[] args) throws InterruptedException {
		ComponentProvider.startComponent();
		
		String serviceDir="D:\\tmp\\services";
		if(args.length>0){
			serviceDir=args[0];
		}
		new ServerContainer(serviceDir).start();
	}
}
