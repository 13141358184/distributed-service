package com.xiaoheiwu.service.transport;

public interface IServerTransport extends ITransport{

	public ITransport accept();
	
	public void bind(int port);
	
	public void setTimeout(int timeout);
}
