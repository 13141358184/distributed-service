package com.xiaoheiwu.service.manager.event;

import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IService;

public class ServiceChangeEvent extends Event{
	public static final String SERVICE_CHANGE_EVENT="SERVICE_CHANGE_EVENT";
	private String path;
	private String name;
	private IService service;
	public ServiceChangeEvent(String path, String name, IService service){
		super(SERVICE_CHANGE_EVENT);
		this.name=name;
		this.service=service;
		this.path=path;
	}
	public String getName() {
		return name;
	}
	public IService getService() {
		return service;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setService(IService service) {
		this.service = service;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
