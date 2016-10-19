package com.xiaoheiwu.service.client.init;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import com.xiaoheiwu.service.client.event.ClientEventType;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.common.log.LogLable;
import com.xiaoheiwu.service.common.performance.PerformanceEvent;
import com.xiaoheiwu.service.common.util.ClassFinderUtil;
import com.xiaoheiwu.service.manager.configure.IServiceConfigure;
import com.xiaoheiwu.service.manager.context.ApplicationContext;
import com.xiaoheiwu.service.manager.event.NodeChangeEvent;
import com.xiaoheiwu.service.manager.event.ServiceChangeEvent;
import com.xiaoheiwu.service.serializer.util.ClassUtil;

public class SystemInitEvent extends Event implements IListerner{
	private Log logger=Log.getLogger(this.getClass());
	private IServiceConfigure serviceConfigure=ComponentProvider.getInstance(IServiceConfigure.class);
	private ApplicationContext applicationContext=ApplicationContext.getInstance();
	public SystemInitEvent() {
		super(ClientEventType.SYSTEM_SERVICE_INIT_EVENT.getEvent());
		addListerner(this.getEvent(), this);
	}

	@Override
	public void execute(IEvent event) {
		IListerner listerner=getListerner(event);
		addListerner(ClientEventType.CALL_SEND_EVENT.getEvent(), listerner);
		addListerner(ClientEventType.CALL_RECEIVE_EVENT.getEvent(), listerner);
		Event.addListerner(NodeChangeEvent.NODE_CHANGE_EVENT, (IListerner)serviceConfigure);
		Event.addListerner(ServiceChangeEvent.SERVICE_CHANGE_EVENT, (IListerner)serviceConfigure);
		loadApplicationContext();
	}

	private void loadApplicationContext() {
		URL url = this.getClass().getClassLoader().getResource("client.properties");
		File file=new File(url.getFile());
		try {
			applicationContext.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private IListerner getListerner(IEvent event) {
		IListerner listerner=new IListerner() {
			
			@Override
			public void execute(IEvent event) {
				if(!PerformanceEvent.class.isInstance(event)){
					logger.error("期望是PerformanceEven,实际上是："+event);
					return ;
				}
				PerformanceEvent performanceEvent=(PerformanceEvent)event;
				String serviceName=performanceEvent.getServiceName();
				String methodName=performanceEvent.getMethodName();
				long callId=performanceEvent.getCallId();
				String eventType=performanceEvent.getEnventType();
				if(ClientEventType.CALL_SEND_EVENT.getEvent().equals(event.getEvent())){
					logger.info(LogLable.PERFORMANCE_SERVICE_BEFORE_EXECUTE,serviceName,methodName,callId,System.currentTimeMillis());
				}else if(ClientEventType.CALL_RECEIVE_EVENT.getEvent().equals(event.getEvent())){
					logger.info(LogLable.PERFORMANCE_SERVICE_AFTER_EXECUTE,serviceName,methodName,callId,System.currentTimeMillis());
				}
			}
		};
		return listerner;
	}



}
