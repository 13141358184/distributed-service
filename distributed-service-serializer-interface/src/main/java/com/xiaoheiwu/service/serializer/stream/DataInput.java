package com.xiaoheiwu.service.serializer.stream;

public interface DataInput {
	
	public int readInt();
	
	public long readLong();
	
	public byte readByte();
	
	public double readDouble();
	
	public String readString();
	
	public boolean readBoolean();
	
	public byte[] readBytes(int size);
	
	public byte[] getOrigData();
}
