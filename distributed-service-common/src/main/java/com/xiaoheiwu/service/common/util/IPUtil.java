package com.xiaoheiwu.service.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {

	public static String getIp() {
		String ipAddress = "127.0.0.1";
		try {
			Enumeration allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						if (!ip.getHostAddress().equals("127.0.0.1")) {
							ipAddress = ip.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return ipAddress;
	}
	

	public static int getPort(SocketChannel channel, boolean isLocal) {
		try{
			if(channel==null)return -1;
			SocketAddress address=null;
			if(isLocal){
				address=channel.getLocalAddress();
			}else{
				address=channel.getRemoteAddress();
			}
			if(InetSocketAddress.class.isInstance(address)){
				InetSocketAddress inetSocketAddress=(InetSocketAddress)address;
				return inetSocketAddress.getPort();
			}
			return -1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
	}
	public static SocketChannel getSocketChannel(Channel channel){
		if(SocketChannel.class.isInstance(channel)){
			SocketChannel socketChannel=(SocketChannel)channel;
			return socketChannel;
		}
		return null;
		
	}
	public static String getIp(SocketChannel channel, boolean isLocal) {
		try{
			if(channel==null)return null;
			SocketAddress address=null;
			if(isLocal){
				address=channel.getLocalAddress();
			}else{
				address=channel.getRemoteAddress();
			}
			if(InetSocketAddress.class.isInstance(address)){
				InetSocketAddress inetSocketAddress=(InetSocketAddress)address;
				return inetSocketAddress.getHostString();
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean hasLocalIp(List<String> ips){
		String localIp=getIp();
		for(String ip: ips){
			if(ip.endsWith(localIp))return true;
		}
		return false;
	}
}
