package com.xiaoheiwu.service.common.log;

import org.apache.log4j.Logger;
/**
 * logger的包装类，委托给logger完成具体操作。在后续扩展时方便
 * @author Chris
 *
 */
public class Log {
	private Logger logger;
	private String commandLable=null;
	public static Log getLogger(String name){
		return new Log(name);
	}
	public static Log getLogger(Class clazz){
		return new Log(clazz.getName());
	}
	public Log(String name){
		logger=Logger.getLogger(name);
	}
	public Log(Class clazz){
		logger=Logger.getLogger(clazz);
	}
	
	public void debug(Object ...strings ){
		if(strings[0].toString().startsWith("COMPONENT")){
			return;
		}
		String content=getContent(strings);
		logger.debug(content);
	}
	public void info(Object ...strings ){
		String content=getContent(strings);
		logger.info(content);
	}
	public void warn(Object ...strings ){
		String content=getContent(strings);
		logger.warn(content);
	}
	public void warn(Throwable e,Object ...strings ){
		String content=getContent(strings);
		logger.warn(content,e);
	}
	private String getContent(Object[] strings) {
		StringBuilder sb=new StringBuilder();
		boolean isFirst=true;
		for(Object object:strings){
			if(isFirst){
				isFirst=false;
			}else{
				sb.append(",");
			}
			sb.append(object);
		}
		return sb.toString();
	}
	public void error(Throwable t,Object ...strings ){
		String content=getContent(strings);
		logger.error(content, t);
	}
	public void error(Object ...strings ){
		String content=getContent(strings);
		logger.error(content);
	}
}
