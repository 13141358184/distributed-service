package com.xiaoheiwu.service.serializer.stream.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.StringChasetCode;


public class ByteDataOutput implements DataOutput{
	private ByteBuffer buffer;
	private int growthCoefficient=2;
	private int initialCapacity=1000;
	protected boolean isRead=false;
	protected byte[] frameData;
	public static DataOutput wrap(byte[] data){
		DataOutput output= new ByteDataOutput(data.length,2);
		output.writeBytes(data);
		return output;
	}
	
	public ByteDataOutput(){
		buffer=ByteBuffer.allocate(initialCapacity);
	}
	public ByteDataOutput(int initialCapacity,int  growthCoefficient){
		buffer=ByteBuffer.allocate(initialCapacity);
		this.growthCoefficient=growthCoefficient;
		this.initialCapacity=initialCapacity;
	}
	@Override
	public void writeInt(int value) {
		resizeIfNecessary(4);
		buffer.putInt(value);
	}

	@Override
	public void writeDouble(double value) {
		resizeIfNecessary(8);
		buffer.putDouble(value);
	}

	@Override
	public void writeLong(long value) {
		resizeIfNecessary(8);
		buffer.putLong(value);
	}

	@Override
	public void writeByte(byte value) {
		resizeIfNecessary(1);
		buffer.put(value);
	}

	@Override
	public void writeBytes(byte[] values) {
		resizeIfNecessary(values.length+4);
//		buffer.putInt(values.length);
//		if(values.length==0)return;
		buffer.put(values);
	}
	@Override
	public boolean resizeIfNecessary(int byteCount){
		if(buffer.remaining()<byteCount){
			int size=buffer.capacity();
			if(byteCount>buffer.capacity()){
				size=byteCount;
			}
			buffer.flip();
			ByteBuffer tmp=ByteBuffer.allocate(size*growthCoefficient);
			tmp.put(buffer);
			buffer.clear();
			buffer=tmp;
			return true;
		}
		return false;
	}
	@Override
	public byte[] getData() {
		if(isRead)return frameData;
		synchronized (this) {
			if(isRead)return frameData;
			buffer.flip();
			byte[] orig= buffer.array();
			byte[] result=Arrays.copyOf(orig, buffer.limit());
			this.frameData=result;
			isRead=true;
			return frameData;
		}
	}
	@Override
	public void flush(OutputStream out) {
		try{
			byte[] frameData=this.getData();
			out.write(frameData);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
			try {
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	@Override
	public void writeString(String value) {
		try {
			byte[] values=value.getBytes(StringChasetCode.CHARSET_CODE);
			resizeIfNecessary(values.length+4);
			writeInt(values.length);
			writeBytes(values);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void writeBoolean(boolean value) {
		resizeIfNecessary(1);
		writeByte((byte)(value?1:0));
	}
	@Override
	public void writeDataOutput(DataOutput ouput) {
		byte[] data=ouput.getData();
		this.writeBytes(data);
	}

}
