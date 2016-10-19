package com.xiaoheiwu.service.transport.transport;

import java.util.HashMap;
import java.util.Map;

import com.xiaoheiwu.service.transport.ITransportArgs;
import com.xiaoheiwu.service.transport.ITransportSocketArgs;

public class SocketTransportArgs extends TransportArgs implements ITransportSocketArgs{
	public static final String IP="ip";
	public static final String PORT="port";
	private String ip;
	private int port;
	public SocketTransportArgs(String ip, int port){
		super(ITransportArgs.SOCKET_TRANSPORT, createArgs(ip, port));
		this.ip=ip;
		this.port=port;
	}
	protected static Map<String, Object> createArgs(String ip, int port){
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put(IP, ip);
		parameters.put(PORT, port);
		return parameters;
	}
	@Override
	public int getTransportType() {
		return ITransportArgs.SOCKET_TRANSPORT;
	}


	
	@Override
	public String getIp() {
		return ip;
	}
	@Override
	public int getPort() {
		return port;
	}

}
