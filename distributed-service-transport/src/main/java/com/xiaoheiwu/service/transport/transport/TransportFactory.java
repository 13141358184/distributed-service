package com.xiaoheiwu.service.transport.transport;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.pool.impl.GenericObjectPool;



import com.xiaoheiwu.service.transport.IServerTransport;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.ITransportArgs;
import com.xiaoheiwu.service.transport.ITransportCreator;
import com.xiaoheiwu.service.transport.ITransportFactory;

public class TransportFactory implements ITransportFactory{
	private static final String LOCAL_IP="localhost";
	private static final TransportFactory factory=new TransportFactory();
	private ConcurrentMap<Integer, ITransportCreator> creators=new ConcurrentHashMap<Integer, ITransportCreator>();
	public TransportFactory(){
		this.creators.put(ITransportArgs.SOCKET_TRANSPORT, new SocketTransportCreator());
	}
	public static TransportFactory getInstance(){
		return factory;
	}
	private Map<String, GenericObjectPool<Channel>> pools=new HashMap<String, GenericObjectPool<Channel>>();
	private volatile int defaultTransportType=ITransportArgs.SOCKET_TRANSPORT;

//	@Override
//	public ITransport getTransport(String serviceName, Integer type) {
//		ITransportArgs args=getTransportArgs(serviceName, type);
//		return getTransport(args);
//	}
//	@Override
//	public ITransport getTransport(String serviceName) {
//		return getTransport(serviceName, defaultTransportType);
//	}
	@Override
	public void addTransportCreator(Integer type,
			ITransportCreator transportCreator) {
		creators.putIfAbsent(type, transportCreator);
		
	}
	@Override
	public void setCurrentTransportType(Integer type) {
		this.defaultTransportType=type;
	}


	@Override
	public IServerTransport openServerTransport() {
		ServerTransprot transport=new ServerTransprot();
		transport.setTimeout(0);
		transport.setTransportFactory(this);
		return transport;
	}
	
	@Override
	public ITransport getTransport(ITransportArgs args) {
		GenericObjectPool<Channel> genericObjectPool = getTransportPool(args);
		Channel channel=null;
		try {
			channel = genericObjectPool.borrowObject();
			ChannelTransport transport=new ChannelTransport(channel, genericObjectPool);
			transport.setTransportFactory(this);
			return transport;
		} catch (Exception e) {
			e.printStackTrace();
			if(channel!=null)
				try {
					genericObjectPool.returnObject(channel);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			return null;
		}
	}

	private GenericObjectPool<Channel> getTransportPool(ITransportArgs args) {
		String identity=args.getIdentity();
		if(!pools.containsKey(identity)){
			synchronized (pools) {
				if(!pools.containsKey(identity)){
					GenericObjectPool<Channel> pool=createGenericObjectPool(args);
					this.pools.put(identity, pool);
				}
			}
		}
		GenericObjectPool<Channel> genericObjectPool=pools.get(identity);
		return genericObjectPool;
	}
	
	protected GenericObjectPool<Channel> createGenericObjectPool(
			ITransportArgs args) {
		ITransportCreator creator = getTransportCreator(args.getTransportType());
		GenericObjectPool<Channel> pool=new GenericObjectPool<Channel>(new PoolabelTransportFactory(creator, args),new PoolConfig());
		return pool;
	}
	protected ITransportCreator getTransportCreator(int type){
		return this.creators.get(type);
	}

	@Override
	public ITransport getLocalTransport(int port) {
		return getTransport(new SocketTransportArgs(LOCAL_IP, port));
	}

}
