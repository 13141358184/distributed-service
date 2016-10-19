package com.xiaoheiwu.service.common.event.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;

public class Event implements IEvent{
	protected String event;
	protected static Map<String, List<IListerner>> listerners=new HashMap<String, List<IListerner>>();
	
	public Event(String event){
		this.event=event;
	}
	@Override
	public String getEvent() {
		return event;
	}

	public static void addListerner(String event, IListerner listerner) {
		List<IListerner> list=listerners.get(event);
		if(list==null){
			list=new ArrayList<IListerner>();
			listerners.put(event, list);
		}
		list.add(listerner);
	}

	@Override
	public void fireEvent() {
		List<IListerner> list=listerners.get(event);
		if(list==null)return;
		for(IListerner listerner: list){
			listerner.execute(this);
		}
	}

}
