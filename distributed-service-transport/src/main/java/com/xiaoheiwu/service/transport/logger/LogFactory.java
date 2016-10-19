package com.xiaoheiwu.service.transport.logger;

import org.apache.log4j.Logger;

public class LogFactory {
	public static Logger getLogger(Class class1){
		return Logger.getLogger(class1);
	}
}
