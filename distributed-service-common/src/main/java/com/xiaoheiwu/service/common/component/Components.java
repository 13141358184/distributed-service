package com.xiaoheiwu.service.common.component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.callchain.CallChainManager;
import com.xiaoheiwu.service.common.callchain.ICallChainManager;
import com.xiaoheiwu.service.common.log.Log;

@Service
public class Components  implements IComponents ,BeanFactoryPostProcessor{
	private Log logger=Log.getLogger(this.getClass());
	private static final IComponents instance=new Components();
	private List<URL> urls=new ArrayList<>();
	private DefaultListableBeanFactory beanFactory;
	private List<BeanPostProcessor> beanProcessors=new ArrayList<>();
	private static ConcurrentMap<Class, String> class2Names=new ConcurrentHashMap();
	private Components(){
		URL url = this.getClass().getClassLoader().getResource("beans.xml");
		urls.add(url);
	}
	public static IComponents getInstance(){
		return instance;
	}
	
	@Override
	public synchronized boolean start() {
		if(this.beanFactory!=null)return true;
		DefaultListableBeanFactory factory=new DefaultListableBeanFactory();  
		for(BeanPostProcessor processor:this.beanProcessors){
			factory.addBeanPostProcessor(processor);
		}
		
	    BeanDefinitionReader bdr=new XmlBeanDefinitionReader((BeanDefinitionRegistry) factory); 
	    for(URL url :this.urls){
		    Resource resource=new UrlResource(url);  
		    bdr.loadBeanDefinitions(resource);
	    }
	    this.beanFactory=factory;
	    this.postProcessBeanFactory(beanFactory);
	    return true;
	}

	@Override
	public boolean stop() {
		this.beanFactory=null;
		return true;
	}

	@Override
	public void addResource(URL... additionalUrls) {
		if(additionalUrls==null||additionalUrls.length==0)return;
		for(URL url: additionalUrls){
			urls.add(url);
		}
	}

	@Override
	public void setComponentScane(IComponentScane scane) {
		List<URL> urlList=scane.findComponentConfigure();
		this.urls.addAll(urlList);
		
	}
	@Override
	public boolean containsBean(String name) {
		return this.beanFactory.containsBean(name);
	}
	@Override
	public String[] getAliases(String name) {
		return  this.beanFactory.getAliases(name);
	}
	@Override
	public Object getBean(String name) throws BeansException {
		return this.beanFactory.getBean(name);
	}
	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return this.beanFactory.getBean(requiredType);
	}
	@Override
	public <T> T getBean(String name, Class<T> requiredType)
			throws BeansException {
		return this.beanFactory.getBean(name, requiredType);
	}
	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return this.beanFactory.getBean(name, args);
	}
	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return this.beanFactory.getType(name);
	}
	@Override
	public boolean isPrototype(String name)
			throws NoSuchBeanDefinitionException {
		return this.beanFactory.isPrototype(name);
	}
	@Override
	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		
		return this.beanFactory.isSingleton(name);
	}
	@Override
	public boolean isTypeMatch(String name, Class<?> targetType)
			throws NoSuchBeanDefinitionException {
		return this.beanFactory.isTypeMatch(name, targetType);
	}
	public static void main(String[] args) {
		IComponents components=new Components();
		components.start();
		ICallChainManager callChainManager= components.getBean(CallChainManager.class);
	}
	@Override
	public <T> void registerComponent(String name, T bean) {
		this.beanFactory.registerSingleton(name, bean);
	}
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanNameGenerator beanNameGenerator=new AnnotationBeanNameGenerator();
		String[] beanDefinitionsNames = beanFactory.getBeanDefinitionNames();
		for(String beanDefinitionsName:beanDefinitionsNames){
		
			BeanDefinition beanDefinition=beanFactory.getBeanDefinition(beanDefinitionsName);
			String className=beanDefinition.getBeanClassName();
			Class class1=null;
			try {
				class1 = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			ServiceDefault serviceDefault=(ServiceDefault) class1.getAnnotation(ServiceDefault.class);
			if(serviceDefault!=null){
				String name=beanNameGenerator.generateBeanName(beanDefinition, null);
				class2Names.put(serviceDefault.value(), name);
				logger.debug("DEFAULT Bean",serviceDefault.value(),name);
			}
		}
		
	}
	@Override
	public void addBeanPostProcessor(BeanPostProcessor... postProcessors) {
		if(postProcessors==null)return;
		for(BeanPostProcessor processor:postProcessors){
			this.beanFactory.addBeanPostProcessor(processor);
		}
	}
	@Override
	public String getBeanName(Class interfaceClass) {
		return class2Names.get(interfaceClass);
	}
	@Override
	public void setDefaultImp(Class interfaceClass, String beanName) {
		this.class2Names.put(interfaceClass, beanName);
	}
	@Override
	public void setDefaultImp(Class interfaceClass, Object newImpl) {
		String beanName=newImpl.getClass().getName();
		this.registerComponent(beanName, newImpl);
		this.setDefaultImp(interfaceClass, beanName);
	}
}
