package com.xiaoheiwu.service.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记实现类是一个组建实现类。可以根据此标记自动创建Provider
 * @author Chris
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Componet {
	
	/**
	 * 
	 * @return是否是组建接口的默认实现
	 */
	boolean isDefaultImpl() default false;
	
	/**
	 * 
	 * @return 组建接口Class
	 */
	Class value();

	/**
	 * 
	 * @return provider的name。每一个组建有3部分组成，接口，实现类和provider。
	 * provider主要是用来创建组建实例的
	 */
	String name() default "";
	
	/**
	 * 
	 * @return 标记此组建是否是一个远程服务
	 */
	boolean isService() default false;
	
	
	/**
	 * 
	 * @return 是否支持环绕通知
	 */
	boolean invocation() default true;
}
