package com.xiaoheiwu.service.server.event;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.router.ILocalServer;
import com.xiaoheiwu.service.server.LocalServer;

import com.xiaoheiwu.service.server.handle.handle.AcceptHandle;
import com.xiaoheiwu.service.server.handle.handle.ReadHandle;
import com.xiaoheiwu.service.server.handle.requesthandle.ConnectionManagerHandle;
import com.xiaoheiwu.service.server.handle.requesthandle.GovernanceRequestHandle;
import com.xiaoheiwu.service.server.handle.requesthandle.InvokeRequestHandle;
import com.xiaoheiwu.service.server.handle.requesthandle.PiplineRequestHandle;
import com.xiaoheiwu.service.server.handle.service.limit.LimitServiceGovernance;
import com.xiaoheiwu.service.server.handle.service.status.StatusServiceGovernance;
import com.xiaoheiwu.service.transport.IServerTransport;
import com.xiaoheiwu.service.transport.handle.ISelectorHandle;
import com.xiaoheiwu.service.transport.handle.impl.AcceptSelectorHandle;
import com.xiaoheiwu.service.transport.selector.ISelector;

public class SystemInitEvent extends Event implements IListerner{
	public static final String SYSTEM_INIT_EVENT="SYSTEM_INIT_EVENT";
	private ISelector selector;
	private  IServerTransport serverTransport;
	private int port;
	public SystemInitEvent(ISelector selector, IServerTransport serverTransport, int port) {
		super(SYSTEM_INIT_EVENT);
		this.selector=selector;
		this.serverTransport=serverTransport;
		this.port=port;
		Event.addListerner(SYSTEM_INIT_EVENT, this);
		
	}
	@Override
	public void execute(IEvent event) {
		final SystemInitEvent systemInitEvent =(SystemInitEvent)event;
		ReadHandle readHandle=new ReadHandle();
		
		ISelectorHandle acceptSelectorHandle=new AcceptSelectorHandle(serverTransport);
		AcceptHandle acceptHandle=new AcceptHandle(readHandle);
		acceptSelectorHandle.addHandle(acceptHandle);
		selector.addAcceptSelectorHandle(acceptSelectorHandle);
		
		final PiplineRequestHandle piplineRequestHandle=new PiplineRequestHandle();
		GovernanceRequestHandle governanceRequestHandle=new GovernanceRequestHandle();
		governanceRequestHandle.addHandle(new LimitServiceGovernance()).addHandle(new StatusServiceGovernance());
		piplineRequestHandle.addHandle(governanceRequestHandle).addHandle(new ConnectionManagerHandle()).addHandle(new InvokeRequestHandle());
		readHandle.setPiplineRequestHandle(piplineRequestHandle);
		
		
		ComponentProvider.register(ILocalServer.class.getName(), new LocalServer(systemInitEvent.getPort(),piplineRequestHandle));
	}
	public int getPort() {
		return port;
	}

}
