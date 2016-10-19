package com.xiaoheiwu.service.transport;

import java.nio.channels.Channel;


public interface ITransportCreator {


	public Channel createChannel(ITransportArgs args);

	
}
