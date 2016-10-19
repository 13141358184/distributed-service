package com.xiaoheiwu.service.common.event.impl;

import java.lang.reflect.Method;


public class InvocationEvent extends Event{
	protected Object target;
	protected Method method;
	protected Object[] args;
	protected Object returnValue;
	protected boolean beforeEvent=true;
	public InvocationEvent(String event) {
		super(event);
	}
	public Object getTarget() {
		return target;
	}
	public Method getMethod() {
		return method;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Object getReturnValue() {
		return returnValue;
	}
	public boolean isBeforeEvent() {
		return beforeEvent;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	public void setBeforeEvent(boolean beforeEvent) {
		this.beforeEvent = beforeEvent;
	}

}
