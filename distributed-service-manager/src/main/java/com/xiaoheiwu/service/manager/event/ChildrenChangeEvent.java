package com.xiaoheiwu.service.manager.event;

import java.util.List;

import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IServiceNode;

public class ChildrenChangeEvent extends Event{
	public static final String CHILDREN_CHANGE_EVENT="CHILDREN_CHANGE_EVENT";
	private String name;
	private List<IServiceNode> nodes;
	public ChildrenChangeEvent(String name, List<IServiceNode> nodes) {
		super(CHILDREN_CHANGE_EVENT);
		this.name=name;
		this.nodes=nodes;
	}
	public String getName() {
		return name;
	}
	public List<IServiceNode> getNodes() {
		return nodes;
	}
	

}
