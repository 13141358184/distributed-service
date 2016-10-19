package com.xiaoheiwu.service.zookeeper;

import java.util.List;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.xiaoheiwu.service.common.log.Log;
/**
 * 	public static final int NodeChildrenChanged=201;
	public static final int NodeCreated=202;
	public static final int NodeDataChanged=203;
	public static final int NodeDeleted=204;
 * @author Chris
 *
 */
public class ZKWatcher implements CuratorWatcher {
	private static final ZKWatcher instance=new ZKWatcher();
	private Log logger=Log.getLogger(this.getClass());
	private ZKWatcher(){
		
	}
	public static ZKWatcher getInstance(){
		return instance;
	}
	@Override
	public void process(WatchedEvent event) throws Exception {
		ZKEvent zkEvent=new ZKEvent(event);
		logger.debug("ZKWATCH",event.getPath(),event.getType(),event.getState());
		if(event.getType()==EventType.NodeChildrenChanged){
			List<String> children=ZKClient.getChildren(event.getPath(),true);
			zkEvent.setChildren(children);
		}else if(event.getType()==EventType.NodeDataChanged){
			Object nodeData=ZKClient.get(zkEvent.getPath(),true);
			zkEvent.setNodeData(nodeData);
		}else if(event.getType()==EventType.NodeDeleted){
			ZKClient.exist(event.getPath(),true);
		}else if(event.getType()==EventType.NodeCreated){
			List<String> children=ZKClient.getChildren(event.getPath(),true);
			Object nodeData=ZKClient.get(zkEvent.getPath(),true);
			zkEvent.setChildren(children);
			zkEvent.setNodeData(nodeData);
		}else {
			throw new RuntimeException("不支持的事件");
		}
		zkEvent.fireEvent();
		
	}

}
