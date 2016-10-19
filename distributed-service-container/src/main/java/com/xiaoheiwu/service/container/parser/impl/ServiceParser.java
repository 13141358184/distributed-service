package com.xiaoheiwu.service.container.parser.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.container.parser.IServiceParser;
import com.xiaoheiwu.service.container.parser.XmlService;
import com.xiaoheiwu.service.container.parser.XmlServiceNode;

@Service
@ServiceDefault(IServiceParser.class)
public class ServiceParser implements IServiceParser{
	private Log logger=Log.getLogger(this.getClass());
	
	public XmlService parseXmlService(String xmlFilePath){
		return parseXmlService(new File(xmlFilePath));
	}
	/**
	 * 把service.xml解析成XmlService对象
	 */
	public XmlService parseXmlService(File xmlFile){
		try{
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(xmlFile);
			Element  rootElement=document.getRootElement();
			XmlService service=parseXmlService(rootElement);
			XmlServiceNode serviceNode=parseServiceNode(rootElement);
			service.setServiceNode(serviceNode);
			return service;
		}catch(Exception e){
			logger.error("解析service.xml异常",e);
			return null;
		}
		
	}
	/**
	 * 解析除了serviceNode外的部分
	 * @param rootElement
	 * @return
	 */
	private XmlService parseXmlService(Element rootElement) {
		XmlService service=new XmlService();
		String interfaceClassName =getAttribute(rootElement, "interfaceClassName");
		String implClassName =getAttribute(rootElement,"implClassName");
		String version =getAttribute(rootElement,"version");
		service.setImplClassName(implClassName);
		service.setInterfaceClassName(interfaceClassName);
		service.setVersion(version);
		Map<String, String> strategys= parseStrategys(rootElement);
		service.setStrategys(strategys);
		return service;
	}
	/**
	 * 解析servieNode
	 * @param rootElement
	 * @return
	 */
	private XmlServiceNode parseServiceNode(Element rootElement) {
		XmlServiceNode serviceNode=new XmlServiceNode();
		Element node=rootElement.getChild("node");
		String ip=getAttribute(node, "ip");
		String port=getAttribute(node, "port");
		Map<String, String> strategys= parseStrategys(node);
		serviceNode.setIp(ip);
		serviceNode.setPort(port);
		serviceNode.setStrategys(strategys);
		return serviceNode;
	}
	
	/**
	 * 解析strategys结构
	 * @param serviceElement strategys的父节点
	 * @return
	 */
	private Map<String, String> parseStrategys(Element serviceElement){
		Element strategysElement=serviceElement.getChild("strategys");
		List<Element> strategyList=strategysElement.getChildren();
		Map<String, String>  strategys=new HashMap<String, String>();
		for(Element element:strategyList){
			Entry<String, String> strategy=parseStrategy(element);
			if(strategy==null) throw new RuntimeException("element parse error "+element);
			strategys.put(strategy.getKey(),strategy.getValue());
		}
		return strategys;
	}
	/**
	 * 解析strategy结构
	 * @param element strategy结构对应的节点
	 * @return
	 */
	private Entry<String, String> parseStrategy(Element element) {
		Element keyElement = element.getChild("key");
		Element valueElement= element.getChild("value");
		if(keyElement==null||valueElement==null)return null;
		final String key= keyElement.getText();
		final String value=valueElement.getText();
		
		return new Entry<String, String>() {
			
			@Override
			public String setValue(String value) {
				return null;
			}
			
			@Override
			public String getValue() {
				return value;
			}
			
			@Override
			public String getKey() {
				return key;
			}
		};
	}

	/**
	 * 解析节点的属性
	 * @param serviceElement 属性对应的节点
	 * @param attributeName  属性名称
	 * @return 属性的值
	 */
	private String getAttribute(Element serviceElement, String attributeName){
		Attribute attribute=serviceElement.getAttribute(attributeName);
		if(attribute==null){
			logger.error("没有"+attributeName+"属性");
			return null;
		}
		return attribute.getValue();
	}

	public static void main(String[] args) {
		IServiceParser parser=new ServiceParser();
		parser.parseXmlService("C:\\ls\\services\\HelloAPI\\service.xml");
	}
}
