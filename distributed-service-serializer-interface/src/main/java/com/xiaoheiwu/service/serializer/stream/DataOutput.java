package com.xiaoheiwu.service.serializer.stream;

import java.io.OutputStream;


public interface DataOutput {
	
	public void writeInt(int value);
	
	public void writeDouble(double value);
	
	public void writeLong(long value);
	
	public void writeByte(byte value);
	
	public void writeBytes(byte[] values);
	
	public void writeString(String value);
	
	public void writeBoolean(boolean value);
	
	public boolean resizeIfNecessary(int byteCount);
	
	public byte[] getData();
	
	public void flush(OutputStream out);
	
	public void writeDataOutput(DataOutput ouput);
}
