package com.xiaoheiwu.service.transport.selector;

import com.xiaoheiwu.service.transport.handle.impl.ReaderSelectorHandle;
import com.xiaoheiwu.service.transport.handle.impl.WriteSelectorHandle;
import com.xiaoheiwu.service.transport.selector.impl.ServiceSelector;

public class SelectorFactory {
	private static final SelectorFactory factory=new SelectorFactory();
	private SelectorFactory(){}
	public static SelectorFactory getInstance(){
		return factory;
	}
	
	public ISelector createReadSelector(){
		return createReadWriteSelector();
	}
	public ISelector createWriteSelector(){
		return createReadWriteSelector();
	}
	public ISelector createReadWriteSelector(){
		ISelector selector=new ServiceSelector();
		selector.addReadSelectorHandle(new ReaderSelectorHandle());
		selector.addWriteSelectorHandle(new WriteSelectorHandle());
		return selector;
	}
	public ISelector createAcceptSelector(){
		ISelector selector=new ServiceSelector();
		selector.addReadSelectorHandle(new ReaderSelectorHandle());
		selector.addWriteSelectorHandle(new WriteSelectorHandle());
		return selector;
	}
}
