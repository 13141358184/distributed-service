package com.xiaoheiwu.service.transport;



public interface ITransportFactory {


	public ITransport getTransport(ITransportArgs args );//根据
	
	public ITransport getLocalTransport(int port);
	
	public IServerTransport openServerTransport();

	public void addTransportCreator(Integer type, ITransportCreator transportCreator);
	
	public void setCurrentTransportType(Integer type);

}
