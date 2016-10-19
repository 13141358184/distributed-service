package com.xiaoheiwu.service.transport.handle;

import com.xiaoheiwu.service.transport.ITransport;

public interface IAcceptHandle extends IHandle{

	public boolean handle(ITransport transport);
}
