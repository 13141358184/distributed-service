package com.xiaoheiwu.service.server.hearbeat;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xiaoheiwu.service.transport.ITransport;

public class HeartBeatService {
	private static final HeartBeatService SERVICE=new HeartBeatService();
	private Logger logger=Logger.getLogger(this.getClass());
	private HeartBeatService(){
		
	}
	public static HeartBeatService getInstance(){
		return SERVICE;
	}
	private Map<ITransport, Long> heartBeatMap=new HashMap<ITransport, Long>();
	public void updateHeartBeat(ITransport transport){
		logger.debug("更新transport心跳时间");
	}
	
}
