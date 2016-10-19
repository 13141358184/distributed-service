package com.xiaoheiwu.service.manager.governance;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.event.ConfigureChangeEvent;
import com.xiaoheiwu.service.manager.impl.ServiceNode;
import com.xiaoheiwu.service.manager.pipline.IPipline;
import com.xiaoheiwu.service.manager.pipline.Pipline;
/**
 * 所有服务治理的容器，其他服务治理只要加入到容器中就会调用。
 * @author Chris
 *
 * @param <T>
 * @param <R>
 */
public class ServiceGovernanceContainer<HANDLE,REQUEST> extends Pipline<HANDLE> implements IListerner,IServiceGovernance<REQUEST>{
	private Map<ServiceConfigureKey, IServiceGovernance<REQUEST>> serviceGovernances=new HashMap<ServiceConfigureKey, IServiceGovernance<REQUEST>>();
	public  ServiceGovernanceContainer(){
		Event.addListerner(ConfigureChangeEvent.CONFIGURE_CHANGE_EVENT, this);
	}
	
	
	@Override
	public boolean handle(REQUEST request) {
		for(IServiceGovernance<REQUEST> handle: serviceGovernances.values()){
			try{
				boolean continueHandle=handle.handle(request);
				if(!continueHandle)return continueHandle;
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return true;
	}
	@Override
	public IPipline<HANDLE> addHandle(HANDLE handle) {
		if(!IServiceGovernance.class.isInstance(handle)){
			return this;
		}
		IServiceGovernance<REQUEST> serviceGovernance=(IServiceGovernance<REQUEST>)handle;
		ServiceConfigureKey configureKey=serviceGovernance.getConfigureKey();
		serviceGovernances.put(configureKey, serviceGovernance);
		this.requestHandles.add(handle);
		return this;
	}
	@Override
	public void execute(IEvent event) {
		if(!ConfigureChangeEvent.class.isInstance(event)){
			return ;
		}
		ConfigureChangeEvent changeEvent=(ConfigureChangeEvent)event;
		String serviceName=changeEvent.getServiceName();
		Iterator<Entry<ServiceConfigureKey, IServiceGovernance<REQUEST>>> it = serviceGovernances.entrySet().iterator();
		while(it.hasNext()){
			Entry<ServiceConfigureKey, IServiceGovernance<REQUEST>> entry=it.next();
			ServiceConfigureKey configureKey=entry.getKey();
			IServiceGovernance<REQUEST> governance=entry.getValue();
			IServiceNode node=changeEvent.getServiceNode(configureKey);
			if(node!=null){
				governance.updateConfigureInfo(serviceName, node);
			}
		}
	}
	@Override
	public void updateConfigureInfo(String serviceName, IServiceNode node) {
		throw new RuntimeException("不支持这个方法");
	}
	@Override
	public ServiceConfigureKey getConfigureKey() {
		throw new RuntimeException("不支持这个方法");
	}

}
