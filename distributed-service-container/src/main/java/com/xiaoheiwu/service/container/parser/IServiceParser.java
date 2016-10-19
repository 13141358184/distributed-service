package com.xiaoheiwu.service.container.parser;

import java.io.File;

public interface IServiceParser {
	
	/**
	 * 把xml转化成对象，不做任何判断，没有的属性或元素设置为null
	 * @param xmlFile 
	 * @return
	 */
	public XmlService parseXmlService(File xmlFile);
	
	/**
	 * 把xml转化成对象，不做任何判断，没有的属性或元素设置为null
	 * @param xmlFilePath 文件全路径 
	 * @return
	 */
	public XmlService parseXmlService(String xmlFilePath);
}
