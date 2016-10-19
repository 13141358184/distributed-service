package com.xiaoheiwu.service.transport.selector.impl;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;






import org.apache.log4j.Logger;

import com.xiaoheiwu.service.transport.ITransport;
import com.xiaoheiwu.service.transport.frame.Frame;
import com.xiaoheiwu.service.transport.frame.IFrame;
import com.xiaoheiwu.service.transport.handle.IHandle;
import com.xiaoheiwu.service.transport.handle.IReadHandle;
import com.xiaoheiwu.service.transport.handle.ISelectorHandle;
import com.xiaoheiwu.service.transport.handle.impl.ReaderSelectorHandle;
import com.xiaoheiwu.service.transport.handle.impl.WriteSelectorHandle;
import com.xiaoheiwu.service.transport.selector.ISelector;


public class ServiceSelector implements ISelector{
	private Logger logger =Logger.getLogger(this.getClass());
	protected Selector selector;
	private long selectTimeOut=-1;
	protected volatile boolean isStarting=true;
	protected volatile ISelectorHandle acceptSelectorHandle;
	protected volatile ISelectorHandle readSelectorHandle=new ReaderSelectorHandle();
	protected volatile ISelectorHandle writetSelectorHandle=new WriteSelectorHandle();
	private ConcurrentLinkedQueue<SelectionKeyRegister> registerQueue=new ConcurrentLinkedQueue<SelectionKeyRegister>();
	public ServiceSelector(){
		this(-1);
	}
	public ServiceSelector(long selectTimeout){
		try {
			this.selectTimeOut=selectTimeout;
			selector=Selector.open();
			Thread thread=new Thread(this);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		select();
	}

	
	@Override
	public boolean select() {
		while (this.isStarting) {
			try {
				doSelect();//做就绪选择
				doRegister();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}


	
	
	/**
	 * 做就绪选择，对于选择成功的做相应的处理
	 * @throws IOException
	 */
	protected  void doSelect() throws IOException{
		logger.debug("开始就绪选择");
		if(selectTimeOut==-1)this.selector.select();
		else this.selector.select(this.selectTimeOut);
		if(selector.selectedKeys().size()==0)return;
	
		Iterator<SelectionKey> selectedKeys = selector.selectedKeys()
				.iterator();
		while (this.isStarting && selectedKeys.hasNext()) {
			SelectionKey key = selectedKeys.next();
			selectedKeys.remove();
			if (!key.isValid() ) {
				cleanupSelectionKey(key);
				continue;
			}
			this.handle(key);
		}
			
	}
	protected void handle(SelectionKey key){
		if(!key.isValid())return;

		if(key.isAcceptable()){
			logger.debug("处理accept请求");
			acceptSelectorHandle.handle(key);
		}else if (key.isValid()&&key.isReadable()){
			logger.debug("处理read请求");
			readSelectorHandle.handle(key);
		}else if(key.isWritable()){
			logger.debug("处理write请求");
			writetSelectorHandle.handle(key);
		}else{
			throw new RuntimeException("不支持这种select事件 :"+key.interestOps()+" "+this.getClass().getSimpleName() );
		}

	}
	
	
	
	@Override
	public void register(ITransport channel, int opts,
			Object attach) {
		Frame frame=new Frame(channel);
		frame.setAttach(attach);
		this.register(channel, opts, frame);
	}
	@Override
	public void register(ITransport channel, int opts) {
		Frame frame=new Frame(channel);
		register(channel, opts,  frame);
	}
	@Override
	public void cleanupSelectionKey(SelectionKey key) {
		IFrame frame = (IFrame) key.attachment();
		key.cancel();
		if (frame == null)
			return;
		frame.close();
	}
	@Override
	public  void register(ITransport transport, int opts, IFrame frame) {
		try {
			if(!transport.isSelectable())return;
			transport.configureBlocking(false);
			SelectionKeyRegister keyRegister=new SelectionKeyRegister();
			keyRegister.frame=frame;
			keyRegister.opts=opts;
			this.registerQueue.add(keyRegister);
			this.wakeup();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	@Override
	public void stop() {
		this.isStarting=false;
	}
	
	
	@Override
	public void wakeup() {
		this.selector.wakeup();
	}
	@Override
	public boolean addAcceptSelectorHandle(ISelectorHandle handle) {
		if(handle.getHandleType()!=IHandle.ACCEPT)return false;
		acceptSelectorHandle=handle;
		return true;
	}
	@Override
	public boolean addReadSelectorHandle(ISelectorHandle handle) {
		if(handle.getHandleType()!=IHandle.READ)return false;
		this.readSelectorHandle=handle;
		return true;
	}
	@Override
	public boolean addWriteSelectorHandle(ISelectorHandle handle) {
		if(handle.getHandleType()!=IHandle.WRITE)return false;
		this.writetSelectorHandle=handle;
		return true;
	}
	@Override
	public void addReadHandle(IReadHandle readHandle) {
		this.readSelectorHandle.addHandle(readHandle);
	}
	protected void doRegister() {
		while(!this.registerQueue.isEmpty()){
			SelectionKeyRegister keyRegister=this.registerQueue.poll();
			doRegister(keyRegister);
		}
	}
	private void doRegister(SelectionKeyRegister keyRegister) {
	
		IFrame frame=keyRegister.frame;
		int opts=keyRegister.opts;
		ITransport transport=frame.getTransport();
		
		SelectionKey key = transport.register(this.selector, opts);
		key.attach(frame);
		logger.debug("完成事件注册，注册的事件是："+opt2String.get(opts));
	}
	private class SelectionKeyRegister{
		private IFrame frame;
		private int opts;
	}
	private static Map<Integer, String> opt2String=new HashMap<Integer, String>();
	static{
		opt2String.put(SelectionKey.OP_ACCEPT, "Accept");
		opt2String.put(SelectionKey.OP_READ, "Read");
		opt2String.put(SelectionKey.OP_WRITE, "Write");
		opt2String.put(SelectionKey.OP_CONNECT, "Connection");
	}
}
