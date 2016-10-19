package com.xiaoheiwu.service.client.router;

import com.xiaoheiwu.service.client.protocol.IClientRequest;
import com.xiaoheiwu.service.client.protocol.IClientResponse;
import com.xiaoheiwu.service.client.protocol.impl.ClientRequest;
import com.xiaoheiwu.service.protocol.IServiceRequest;
import com.xiaoheiwu.service.transport.ITransport;
/**
 * 发送数据,并在数据有返回值时,填充response,并调用response的回调函数
 * @author Chris
 *
 */
public interface IRouter {
	
	/**
	 * 把data通过transpor发送的服务提供方。
	 * @param transport 服务方地址信息
	 * @param data 待发送的数据
	 * @param response 返回结果
	 * @param supportChangeTrasport 如果为true则在发送失败时更换transport重发，否则直接报错
	 */
	public void sendAndReceive(ITransport transport, IClientRequest request,  IClientResponse response, boolean supportChangeTrasport);

	/**
	 * 根据服务名称获取服务方transport
	 * @param serviceCall 服务调用信息
	 * @return trasport
	 */
	public ITransport getTransport(IClientRequest serviceCall);
}
