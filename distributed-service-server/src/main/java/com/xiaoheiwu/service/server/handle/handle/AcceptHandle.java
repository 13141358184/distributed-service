package com.xiaoheiwu.service.server.handle.handle;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.balance.IBalanceManager;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.log.Log;
import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.handle.IAcceptHandle;
import com.xiaoheiwu.service.transport.handle.IReadHandle;
import com.xiaoheiwu.service.transport.selector.ISelector;
import com.xiaoheiwu.service.transport.selector.SelectorFactory;

@Service
@ServiceDefault(IAcceptHandle.class)
public class AcceptHandle implements IAcceptHandle{
	private Log logger =Log.getLogger(this.getClass());
	public static final String READER_SELECTOR_BALLANCE="READER_SELECTOR_BALLANCE";
	private IBalanceManager balanceManager=ComponentProvider.getInstance(IBalanceManager.class);
	private int readSelectorCount=1;
	public AcceptHandle(IReadHandle...handles ){
		readSelectorCount=Runtime.getRuntime().availableProcessors();
		initSelectorBalance(handles);
	}
	public AcceptHandle(int readSelectorCount,IReadHandle...handles ){
		this.readSelectorCount=readSelectorCount;
		initSelectorBalance(handles);
	}


	@Override
	public int getHandleType() {
		return ACCEPT;
	}

	@Override
	public boolean handle(ITransport transport) {
		logger.debug("接收到一个新的连接");
		transport.configureBlocking(false);
		ISelector selector=doBalance();
		selector.register(transport, SelectionKey.OP_READ);
		return true;
	}
	
	public int getReadSelectorCount() {
		return readSelectorCount;
	}
	private ISelector doBalance() {
		ISelector selector=balanceManager.doBalance(READER_SELECTOR_BALLANCE);
		return selector;
	}
	/**
	 * 根据select个数创建select负载均衡器。
	 * @param handles select对应的read
	 */
	private void initSelectorBalance(IReadHandle... handles) {
		if(handles.length==0)handles=new IReadHandle[]{new ReadHandle()};
		List<ISelector> selectors=new ArrayList<>();
		for(int i=0;i<this.readSelectorCount;i++){
			ISelector selector=SelectorFactory.getInstance().createReadSelector();
			for(IReadHandle handle: handles){
				selector.addReadHandle(handle);
			}
			selectors.add(selector);
		}
		balanceManager.addBalance(READER_SELECTOR_BALLANCE, selectors);
	}
}
