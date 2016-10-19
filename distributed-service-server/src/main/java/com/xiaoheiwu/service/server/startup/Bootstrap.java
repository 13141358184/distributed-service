package com.xiaoheiwu.service.server.startup;


import org.apache.log4j.Logger;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.server.IServer;
import com.xiaoheiwu.service.server.NonBlockServer;
import com.xiaoheiwu.service.server.service.IServiceRegistion;
import com.xiaoheiwu.service.server.service.impl.CommandService;

public class Bootstrap {
	private Logger logger=Logger.getLogger(this.getClass());
	protected volatile int port = 8088;
	
	public Bootstrap() {
	}

	public void bind(int port){
		this.port=port;
	}
	public void start() {
		IServer server = new NonBlockServer();
		logger.info("server start ,listern in " + port);
		server.start(port);
	}



}
