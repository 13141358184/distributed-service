package com.xiaoheiwu.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Service {

	public String value();//服务名字
	
	public String version() default "1.0.1";//接口version，在接口签名发生变化时，一定要修改version
}
