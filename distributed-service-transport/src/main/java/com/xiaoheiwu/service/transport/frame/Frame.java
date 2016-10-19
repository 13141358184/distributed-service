package com.xiaoheiwu.service.transport.frame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

import com.xiaoheiwu.service.transport.ITransport;


/**
 * Hello world!
 *
 */
public class Frame implements IFrame{
	protected ITransport transport;
	protected FrameState state=FrameState.READ_SIZE;
	protected ByteBuffer dateSize = ByteBuffer.allocate(4);
	protected ByteBuffer readDate = null;
	protected ByteBuffer writeDate = null;
	protected Object attach;
	
	public Frame(ITransport byteChannel){
		this.transport=byteChannel;
	}
	
	@Override
	public ITransport getTransport() {
		return this.transport;
	}

	@Override
	public int read() {
		if(state==FrameState.READ_SIZE){
			int count= internalRead(this.dateSize);
			 if (dateSize.remaining() == 0) {
				 dateSize.flip();
				 int frameSize = dateSize.getInt(0);
				 if(frameSize<0)throw new RuntimeException("Read an invalid frame size of " + frameSize);
				 if(frameSize>IFrame.MAX_READ_BYTE_SIZE)throw new RuntimeException("超过最大帧的限制，最大限制为："+IFrame.MAX_READ_BYTE_SIZE+"。你目前的大小为：" + frameSize);
				 this.dateSize=null;
				 this.readDate=ByteBuffer.allocate(frameSize);
				 state=FrameState.READ_DATE;
			 }
			return count;
			
		}else if(state==FrameState.READ_DATE){
			int count= internalRead(this.readDate);
			 if (readDate.remaining() == 0) {
				 state=FrameState.READ_FINISH;
			 }
			 return count;
		}
		return 0;
	}

	@Override
	public byte[] getDate() {
		byte[] data=new byte[readDate.capacity()];
		if(state==FrameState.READ_FINISH){
			readDate.flip();
			readDate.get(data);
			return data;
		}
		throw new RuntimeException("没有完整的数据可以读取");
	}

	@Override
	public boolean readFinish() {
		return state==FrameState.READ_FINISH;
	}

	@Override
	public boolean write(byte[] message) {
		writeDate=ByteBuffer.allocate(message.length+4);
		writeDate.putInt(message.length);
		writeDate.put(message);
		readDate=null;
		state=FrameState.WRITE_READY;
		return true;
	}

	@Override
	public boolean flush() {
		if(state!=FrameState.WRITE_READY)return false;
		try {
			writeDate.flip();
			transport.write(writeDate);
			writeDate.clear();
			dateSize=ByteBuffer.allocate(4);
			state=FrameState.READ_SIZE;
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	private int internalRead(ByteBuffer buffer) {
	      try {
	    	int size=transport.read(buffer);
	        if (size < 0) {
	          return -1;
	        }
	        return size;
	      } catch (IOException e) {
	    	 e.printStackTrace();
	    	this.close();
	        throw new RuntimeException(e);
	      }
	    }

	@Override
	public void close() {
		try {
			transport.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public FrameState getFrameState() {
		return state;
	}


	@Override
	public boolean writeAndFlush(byte[] message) {
		this.write(message);
		this.flush();
		return true;
	}

	public Object attach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}
}
