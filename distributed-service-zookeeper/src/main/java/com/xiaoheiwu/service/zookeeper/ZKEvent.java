package com.xiaoheiwu.service.zookeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.xiaoheiwu.service.common.event.impl.Event;

public class ZKEvent extends Event{
	public static final String NodeChildrenChanged="NodeChildrenChanged";
	public static final String NodeCreated="NodeCreated";
	public static final String NodeDataChanged="NodeDataChanged";
	public static final String NodeDeleted="NodeDeleted";
	private static Map<EventType, String> types=new HashMap<EventType, String>();
	private String path;
	private EventType type;
	private KeeperState keeperState;
	private WatchedEvent watchedEvent;
	private List<String> children;
	private Object nodeData;
	static{
		types.put(EventType.NodeChildrenChanged, NodeChildrenChanged);
		types.put(EventType.NodeCreated, NodeCreated);
		types.put(EventType.NodeDataChanged, NodeDataChanged);
		types.put(EventType.NodeDeleted, NodeDeleted);
	}
	public ZKEvent(WatchedEvent watchedEvent) {
		super(types.get(watchedEvent.getType()));
		this.watchedEvent=watchedEvent;
		this.path=this.watchedEvent.getPath();
		this.type=watchedEvent.getType();
		this.keeperState=watchedEvent.getState();
	}
	public String getPath() {
		return path;
	}
	public EventType getType() {
		return type;
	}
	public KeeperState getKeeperState() {
		return keeperState;
	}
	public WatchedEvent getWatchedEvent() {
		return watchedEvent;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public void setKeeperState(KeeperState keeperState) {
		this.keeperState = keeperState;
	}
	public void setWatchedEvent(WatchedEvent watchedEvent) {
		this.watchedEvent = watchedEvent;
	}
	public List<String> getChildren() {
		return children;
	}
	public Object getNodeData() {
		return nodeData;
	}
	public void setChildren(List<String> children) {
		this.children = children;
	}
	public void setNodeData(Object nodeData) {
		this.nodeData = nodeData;
	}
	public <T> T getNodeData(Class<T> clazz) {
		if(!clazz.isInstance(nodeData)){
			throw new RuntimeException("不能做类型转换");
		}
		return (T)nodeData;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("path="+path+"\r\n"+"eventType="+this.event+"\r\n");
		sb.append("children:\r\n");
		if(this.children!=null){
			for(String c:children){
				sb.append("	"+c+"\r\n");
			}
		}
		if(this.getNodeData()!=null){
			sb.append("nodeData="+this.nodeData+"\r\n");
		}
		return sb.toString();
	}
	

}
