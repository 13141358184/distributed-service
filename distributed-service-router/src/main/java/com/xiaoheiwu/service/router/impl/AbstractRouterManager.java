package com.xiaoheiwu.service.router.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoheiwu.service.balance.BalanceType;
import com.xiaoheiwu.service.balance.IAccessFilter;
import com.xiaoheiwu.service.balance.IBalance;
import com.xiaoheiwu.service.balance.IBalanceManager;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.event.IEvent;
import com.xiaoheiwu.service.common.event.IListerner;
import com.xiaoheiwu.service.common.event.impl.Event;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.common.util.IPUtil;
import com.xiaoheiwu.service.manager.IServiceManager;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.event.ChildrenChangeEvent;
import com.xiaoheiwu.service.protocol.IProtocolService;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.protocol.IServiceResponse;
import com.xiaoheiwu.service.router.ILocalServer;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.IRouterReceiver;
import com.xiaoheiwu.service.router.RouterGovernanceContainer;
import com.xiaoheiwu.service.router.event.RouterEvent;
import com.xiaoheiwu.service.router.graypublish.AccessServiceGovernance;
import com.xiaoheiwu.service.router.graypublish.RearAccessServiceGovernance;
import com.xiaoheiwu.service.router.graypublish.RearRefuseServiceGovernance;
import com.xiaoheiwu.service.router.graypublish.RefuseServiceGovernance;
import com.xiaoheiwu.service.router.graypublish.VersionMatchServiceGovernance;
import com.xiaoheiwu.service.router.graypublish.VersionRangeServiceGovernance;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.ITransportArgs;
import com.xiaoheiwu.service.transport.ITransportFactory;
import com.xiaoheiwu.service.transport.frame.Frame;
import com.xiaoheiwu.service.transport.frame.IFrame;
import com.xiaoheiwu.service.transport.transport.SocketTransportArgs;
import com.xiaoheiwu.service.transport.transport.TransportFactory;

public abstract class AbstractRouterManager  implements IRouterManager,IListerner{

	protected ITransportFactory factory=TransportFactory.getInstance();
	protected Log logger=Log.getLogger(this.getClass());
	protected IProtocolService protocolService=ComponentProvider.getInstance(IProtocolService.class);
	private IServiceManager serviceManager=ComponentProvider.getInstance(IServiceManager.class);
	private IBalanceManager balanceManager=ComponentProvider.getInstance(IBalanceManager.class);
	private Map<String, Boolean> serverNodes=new HashMap<String, Boolean>();
	private RouterGovernanceContainer container;
	public AbstractRouterManager(){
		Event.addListerner(ChildrenChangeEvent.CHILDREN_CHANGE_EVENT, this);
		container=RouterGovernanceContainer.getInstance();
		container.addHandle(new AccessServiceGovernance());
		container.addHandle(new RefuseServiceGovernance());
		container.addHandle(new RearAccessServiceGovernance());
		container.addHandle(new RearRefuseServiceGovernance());
		container.addHandle(new VersionMatchServiceGovernance());
		container.addHandle(new VersionRangeServiceGovernance());
	}
	@Override
	public ITransport getTransport(IServiceRequest request) {
		IServiceNode node=getServiceNode(request);
		if(node==null)throw new RuntimeException("没有可用的连接");
		ITransportArgs args=new SocketTransportArgs(node.getIp(), node.getPort());
		ITransport transport=factory.getTransport(args);
		logger.debug("ROUTER_TRANSPORT",transport.toString());
		return transport;
	}


	@Override
	public boolean isJVMRouter(IServiceRequest request) {
		String serviceName=request.getServiceName();
		ILocalServer localServer=ComponentProvider.getInstance(ILocalServer.class);
		if(localServer==null)return false;
		if(!localServer.containService(serviceName))return false;
		return true;
	}
	@Override
	public boolean isLocalRouter(IServiceRequest request) {
		List<IServiceNode> nodes=getServiceNodes(request);
		if(nodes==null||nodes.size()==0)return false;
		List<String> ips=new ArrayList<>();
		for(IServiceNode node:nodes){
			ips.add(node.getIp());
		}
		return IPUtil.hasLocalIp(ips);

	}
	@Override
	public IFrame send(ITransport transport, IServiceResponse response)
			throws IOException {
//		String routeFlag=getRouterFlag(response);
//		if(JVM_ROUTE_FLAG.equals(routeFlag)){
//			return JVMRouterManager.getInstance().send(transport, response);
//		}
		byte[] data=protocolService.doReturn(response.createSerializableResponse());
		return send(transport, data, null);
	}

	
	@Override
	public IFrame send(ITransport transport,IServiceRequest request,
			IRouterReceiver receiver) throws IOException {
//		if(isJVMRouter(request)){
//			setRouterlag(request, JVM_ROUTE_FLAG);
//			return JVMRouterManager.getInstance().send(transport, request, receiver);
//		}else if (isLocalRouter(request)){
//			return LocalRouteManager.getInstance().send(transport, request, receiver);
//		}
		byte[] data=protocolService.doCall(request.createSerializableRequest());
		return send(transport, data,receiver);
	}
	
	
	
	protected  IFrame send(ITransport transport, byte[] data,
			IRouterReceiver receiver){
		Frame frame=new Frame(transport);
		frame.write(data);
		frame.flush();
		frame.setAttach(receiver);
		return frame;
	}
	protected List<IServiceNode> getServiceNodes(IServiceRequest request){
		IBalance<IServiceNode> balance=getBalance(request);
		return balance.getElements();
	}
	
	protected IServiceNode getServiceNode(IServiceRequest request) {
		getBalance(request);
		return balanceManager.doBalance(request.getServiceName());
	}
	
	protected IBalance<IServiceNode> getBalance(IServiceRequest request){
		String serviceName=request.getServiceName();
		if(!serverNodes.containsKey(serviceName)){
			return addBalance(serviceName);
		}
		return balanceManager.getBalance(request.getServiceName());
	}
	private synchronized IBalance<IServiceNode> addBalance(String name) {
		if(serverNodes.containsKey(name)){
			return balanceManager.getBalance(name);
		}
		serverNodes.put(name, true);
		return updateBalance(name);
	}
	
	private synchronized IBalance<IServiceNode> updateBalance(String name){
		List<IServiceNode> nodes=serviceManager.getSeviceNodes(name,true);
		balanceManager.addBalance(name, nodes,container,BalanceType.POLLING);
		return balanceManager.getBalance(name);
	}
	@Override
	public void execute(IEvent event) {
		if(!ChildrenChangeEvent.class.isInstance(event)){
			logger.error("不是期望的事件，期望事件类型为:"+ChildrenChangeEvent.CHILDREN_CHANGE_EVENT+";实际上是："+event.getEvent());
			return;
		}
		ChildrenChangeEvent nodeChangeEvent=(ChildrenChangeEvent)event;
		updateBalance(nodeChangeEvent.getName());
	}
	protected class RequestAccessFilter implements IAccessFilter<IServiceNode>{
		private IServiceRequest request;
		public RequestAccessFilter(IServiceRequest request){
			this.request=request;
		}
		@Override
		public boolean enable(IServiceNode node) {
			new RouterEvent(request, node).fireEvent();
			return true;
		}
	}
	
	protected void setRouterlag(IServiceRequest request, String flag){
		request.getAdditionalParameters().put(ROUTE_FLAG, flag);
	}
	private String getRouterFlag(IServiceResponse response) {
		Object object=response.getAdditionalParameters().get(ROUTE_FLAG);
		if(object==null)return null;
		return (String )object;
	}
}
