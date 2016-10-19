package com.xiaoheiwu.service.common.event.impl;


import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;

public abstract class InvocationListerner implements IListerner{

	@Override
	public void execute(IEvent event) {
		if(!InvocationEvent.class.isInstance(event))return;
		InvocationEvent invocationEvent=(InvocationEvent)event;
		if(invocationEvent.beforeEvent){
			executeBeforeEvent(invocationEvent);
		}else{
			executeAfterEvent(invocationEvent);
		}
	}

	protected abstract void executeAfterEvent(InvocationEvent invocationEvent) ;

	protected abstract void executeBeforeEvent(InvocationEvent invocationEvent);

}
