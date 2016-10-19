package com.xiaoheiwu.service.transport.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import com.xiaoheiwu.service.transport.ITransportArgs;
import com.xiaoheiwu.service.transport.ITransportCreator;
import com.xiaoheiwu.service.transport.ITransportSocketArgs;

public class SocketTransportCreator implements ITransportCreator{
	@Override
	public Channel createChannel(ITransportArgs args) {
		if(!ITransportSocketArgs.class.isInstance(args)){
			throw new RuntimeException("不是ITransportSocketArgs参数类型");
		}
		ITransportSocketArgs socketArgs=(ITransportSocketArgs)args;
		String ip=socketArgs.getIp();
		int port=socketArgs.getPort();
		SocketChannel socketChannel=null;
		try {
			socketChannel=SocketChannel.open();
			SocketAddress address=new InetSocketAddress(ip,port);
			socketChannel.connect(address);
		} catch (IOException e) {
			e.printStackTrace();
			if(socketChannel!=null)
				try {
					socketChannel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return socketChannel;
	}

//	@Override
//	public ITransportArgs createArgs(String serviceName) {
//		IServiceNode node=serviceManager.getServerNode(serviceName);
//		return new SocketTransportArgs(node.getIp(), node.getPort());
//	}

}
