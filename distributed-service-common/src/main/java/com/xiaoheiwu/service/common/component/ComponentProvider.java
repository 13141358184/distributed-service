package com.xiaoheiwu.service.common.component;



import java.net.URL;

import org.springframework.util.StringUtils;

import com.xiaoheiwu.service.common.log.Log;


public class ComponentProvider{
	private Log logger=Log.getLogger(this.getClass());
	private static IComponents components=Components.getInstance();


	/**
	 * 获取接口或实现类对应的实例
	 * @param clazz 先通过class2Names获取name；如果没有，则交给spring获取对应的bean
	 * @return 接口对应的实例
	 */
	public static <T> T getInstance(Class<T> clazz){
		String name=components.getBeanName(clazz);
		T t=null;
		if(!StringUtils.isEmpty(name)){
			t=(T)getInstance(name);
		}
		if(t!=null){
			return t;
		}
	
		return components.getBean(clazz);
		
	}
	public static Object getInstance(String name){
		return components.getBean(name);
	}
	
	public static void startComponent(URL... urls){
		if(urls!=null)components.addResource(urls);
		startComponent();
	}
	public static void startComponent(IComponentScane componentScane, URL...urls ){
		if(componentScane!=null)components.setComponentScane(componentScane);
		if(urls!=null)components.addResource(urls);
		startComponent();
	}
	private static void startComponent(){
		components.start();
	}
	/**
	 * 注册某个接口的provider
	 * @param interfaceClass 接口对应的class
	 * @param providerName provider name
	 * @param provider 创建interface的provider
	 */
	public static void register(String name, Object object){
		components.registerComponent(name, object);
	}

	public static void setDefaultImp(Class interfaceClass, String beanName){
		components.setDefaultImp(interfaceClass, beanName);
	}
	
	public static void setDefaultImp(Class interfaceClass, Object newImpl){
		components.setDefaultImp(interfaceClass, newImpl);
	}
	
	
}
