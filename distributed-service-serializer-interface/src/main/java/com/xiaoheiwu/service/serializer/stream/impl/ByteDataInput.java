package com.xiaoheiwu.service.serializer.stream.impl;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.StringChasetCode;



public class ByteDataInput implements DataInput{
	protected ByteBuffer readData;
	protected byte[] data;
	public ByteDataInput(byte[] data){
		this.readData = ByteBuffer.wrap(data);
		this.data=data;
	}
	@Override
	public int readInt() {
		return readData.getInt();
	}
	@Override
	public long readLong() {
		return readData.getLong();
	}
	@Override
	public byte readByte() {
		return readData.get();
	}
	@Override
	public double readDouble() {
		return readData.getDouble();
	}
	@Override
	public byte[] readBytes(int size) {
		byte[] dst=new byte[size];
		readData.get(dst);
		return dst;
	}
	@Override
	public String readString() {
		int size=readInt();
		byte[] values=readBytes(size);
		try {
			return new String(values,StringChasetCode.CHARSET_CODE);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public boolean readBoolean() {
		boolean result=readByte()>0?true:false;
		return result;
	}
	@Override
	public byte[] getOrigData() {
		return data;
	}
}
