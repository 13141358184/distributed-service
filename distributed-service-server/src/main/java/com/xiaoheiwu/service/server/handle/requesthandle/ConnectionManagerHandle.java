package com.xiaoheiwu.service.server.handle.requesthandle;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.server.handle.IRequestHandle;
import com.xiaoheiwu.service.server.protocol.IServerRequest;
import com.xiaoheiwu.service.transport.ITransport;

public class ConnectionManagerHandle implements Runnable, IRequestHandle{
	private Log logger=Log.getLogger(ConnectionManagerHandle.class);
	private ConcurrentMap<ITransport, Long> transports=new ConcurrentHashMap<ITransport, Long>();
	private int timeoutSecond=100000;
	public ConnectionManagerHandle(){
		Thread thread=new Thread(this);
		thread.start();
	}

	@Override
	public boolean handle(IServerRequest request) {
		ITransport transport=request.getTransport();
		transports.put(transport, System.currentTimeMillis());
		return true;
	}

	@Override
	public void run() {
		while(true){
			checkTimeoutTransport();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	protected void checkTimeoutTransport(){
		Iterator<ITransport> it = transports.keySet().iterator();
		while (it.hasNext()){
			ITransport transport=it.next();
			Long lastUpdateTime=transports.get(transport);
			if((System.currentTimeMillis()-lastUpdateTime)>timeoutSecond*1000){
				try {
				
					it.remove();
					logger.info("close transport "+transport.toString());
					transport.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
