package com.xiaoheiwu.service.container.container.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xiaoheiwu.service.annotation.util.ServiceUtils;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.component.Components;
import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.common.configure.impl.Configure;
import com.xiaoheiwu.service.common.executor.MutilExecutors;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.common.util.IPUtil;
import com.xiaoheiwu.service.container.IContainerConfigure;
import com.xiaoheiwu.service.container.classloader.ClassLoaderFactory;
import com.xiaoheiwu.service.container.classloader.ServiceClassLoader;
import com.xiaoheiwu.service.container.container.IServiceContainer;
import com.xiaoheiwu.service.container.parser.IServiceParser;
import com.xiaoheiwu.service.container.parser.XmlService;
import com.xiaoheiwu.service.container.parser.XmlServiceNode;
import com.xiaoheiwu.service.container.util.PathUtil;
import com.xiaoheiwu.service.manager.IService;
import com.xiaoheiwu.service.manager.IServiceManager;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.context.ApplicationContext;
import com.xiaoheiwu.service.manager.impl.Service;
import com.xiaoheiwu.service.manager.impl.ServiceNode;
import com.xiaoheiwu.service.server.invoker.Invoker;
import com.xiaoheiwu.service.server.service.IServiceRegistion;
import com.xiaoheiwu.service.server.service.impl.ServiceRegistion;


public class ServiceContainer implements IServiceContainer{
	private Log logger=Log.getLogger(this.getClass());
	private IServiceNode serviceNode;
	private IService service;
	private Object serviceImpl;
	private Class serviceClass;
	private IServiceManager serviceManager=ComponentProvider.getInstance(IServiceManager.class);
	private IServiceParser serviceParser=ComponentProvider.getInstance(IServiceParser.class);
	@Autowired
	private IServiceRegistion serviceRegistion=Components.getInstance().getBean(IServiceRegistion.class);
	private ServiceClassLoader serviceClassLoader;
	private String baseDir;
	private String serviceDir;
	private int port;
	private IConfigure parentConfigure;
	public ServiceContainer(String baseDir, String serviceDir, int port){
		this(baseDir, serviceDir, port, new Configure());
	}
	public ServiceContainer(String baseDir, String serviceDir, int port, IConfigure parentConfigure){
		this.port=port;
		this.baseDir=baseDir;
		this.serviceDir=serviceDir;
		this.serviceClassLoader=ClassLoaderFactory.getInstance().createServiceClassLoader(baseDir, serviceDir);
		this.serviceNode=parseServiceNode();
		this.service=serviceNode.getService();
		this.parentConfigure=parentConfigure;
		Thread.currentThread().setContextClassLoader(serviceClassLoader);
	}
	@Override
	public boolean start() {
		try{
			IConfigure configure=getConfigure();
			ExecutorService executorService=configure.getObjectValue(IContainerConfigure.SERVICE_THREAD_SERVICE);
			serviceRegistion.registe(this.service.getName(), new Invoker(serviceImpl,executorService,configure));
			serviceManager.publishServiceNode(service, serviceNode);
			ApplicationContext.getInstance().setServiceNode(service.getName(), serviceNode);
			serviceManager.listernService(service);
			serviceManager.listernServiceNode(serviceNode);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}


	

	@Override
	public boolean stop() {
		return true;
	}

	@Override
	public IServiceNode getServiceNode() {
		return this.serviceNode;
	}
	/**
	 * 把xmlservice转化成iservicenode
	 * @return IServiceNode
	 */
	private IServiceNode parseServiceNode() {
		String xmlFilePath=PathUtil.getPath(baseDir, serviceDir)+File.separator+"service.xml";
		XmlService xmlService=serviceParser.parseXmlService(xmlFilePath);
		String interfaceClassName=xmlService.getInterfaceClassName();
		if(interfaceClassName==null){
			logger.error("没有interfaceClassName属性");
			return null;
		}
		serviceClass=loadServiceClass(interfaceClassName);
		this.serviceImpl=newServiceImpl(xmlService.getImplClassName());
		if(serviceClass==null)return null;
		Service service=parseService(xmlService,serviceClass);
		ServiceNode serviceNode=parseServiceNode(xmlService.getServiceNode());
		serviceNode.setService(service);
		serviceNode.setPort(this.port);
		return serviceNode;
	}
	
	private Object newServiceImpl(String implClassName) {
		Class impClass=loadServiceClass(implClassName);
		if(impClass==null)return null;
		
		try {
			return impClass.newInstance();
		} catch (Exception e) {
			logger.error("初始化服务实例失败",implClassName);
			return null;
		} 
	}
	/**
	 * 通过容器自有的类加载器加载服务的接口
	 * @param interfaceClassName 服务的接口
	 * @return
	 */
	public Class loadServiceClass(String interfaceClassName) {
		try{
			return this.serviceClassLoader.loadClass(interfaceClassName);
		}catch(Exception e){
			logger.error("不能加载serviceClass",e);
			return null;
		}
	}
	/**
	 * 解析Service，如果接口用了@Service,则可以不用配name，version
	 * @param xmlService
	 * @param serviceClass
	 * @return
	 */
	private Service parseService(XmlService xmlService, Class serviceClass) {
		Service service=new Service();
		String serviceName=xmlService.getServiceName();
		if(serviceName==null){
			serviceName=ServiceUtils.getServiceName(serviceClass);
		}
		String version =xmlService.getVersion();
		if(version==null){
			version=ServiceUtils.getVersion(serviceClass);
		}
		service.setName(serviceName);
		service.setVersion(version);
		service.setStrategy(xmlService.getStrategys());
		return service;
	}
	/**
	 * 解析serviceNode，ip可以不用配置，如果不配置用本机ip；port可以不用配置，不配置默认8087
	 * @param xmlServiceNode 
	 * @return
	 */
	private ServiceNode parseServiceNode(XmlServiceNode xmlServiceNode) {
		String ip=xmlServiceNode.getIp();
		if(ip==null){
			ip=IPUtil.getIp();
		}
		String portString=xmlServiceNode.getPort();
		if(portString==null){
			portString="8087";
		}
		int port=-1;
		try{
			port=Integer.valueOf(portString);
		}catch(Exception e){
			logger.error("port不是int"+portString);
			return  null;
		}
	
		ServiceNode serviceNode=new ServiceNode(ip,port);
		serviceNode.setStrategy(xmlServiceNode.getStrategys());
		return serviceNode;
	}
	public IService getService() {
		return service;
	}
	public Class getServiceClass() {
		return serviceClass;
	}
	public Object getServiceImpl() {
		return serviceImpl;
	}
	private ExecutorService getExecutorService() {
		Integer serviceThreadCount=serviceNode.getConfigure().getIntValue(IContainerConfigure.SERVICE_THREAD_COUNT);
		ExecutorService executorService;
		if(serviceThreadCount==null){
			executorService=parentConfigure.getObjectValue(IContainerConfigure.SERVICE_THREAD_SERVICE);
		}
		else{
			executorService=MutilExecutors.newFixedThreadPool(serviceThreadCount);
		}
		return executorService;
	}
	private IConfigure getConfigure() {
		ExecutorService executorService=getExecutorService();
		if(executorService==null)executorService=MutilExecutors.newFixedThreadPool(1);
		IConfigure configure=new Configure();
		configure.load(parentConfigure.getMapValue());
		configure.load(serviceNode.getConfigure().getMapValue());
		configure.putValue(IContainerConfigure.SERVICE_THREAD_SERVICE, executorService);
		return configure;
	}
	public void setServiceRegistion(IServiceRegistion serviceRegistion) {
		this.serviceRegistion = serviceRegistion;
	}
	
}
