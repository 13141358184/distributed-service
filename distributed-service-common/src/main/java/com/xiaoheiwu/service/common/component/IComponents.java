package com.xiaoheiwu.service.common.component;

import java.net.URL;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.xiaoheiwu.service.common.lifecycle.ILifecycle;

public interface IComponents extends ILifecycle,BeanFactory{

	/**
	 * 增加spring配置文件, 需要在start之前。start之后调用将被忽略
	 * @param urls 增加的配置文件
	 */
	public void addResource(URL... urls);
	
	/**
	 * 提供组件搜索器，搜索配置文件
	 * @param scane
	 */
	public void setComponentScane(IComponentScane scane);
	
	
	public <T> void registerComponent(String name, T bean);
	
	public String getBeanName(Class interfaceClass);
	
	
	public void addBeanPostProcessor(BeanPostProcessor... postProcessors);
	
	public void setDefaultImp(Class interfaceClass, String beanName);
	
	public void setDefaultImp(Class interfaceClass, Object newImpl);
}
