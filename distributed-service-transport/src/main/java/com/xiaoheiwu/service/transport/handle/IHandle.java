package com.xiaoheiwu.service.transport.handle;


public interface IHandle {
	public static final int READ=3;
	public static final int WRITE=2;
	public static final int ACCEPT=1;
	public int getHandleType();
	

}
