package com.xiaoheiwu.service.serializer.serializer.readwriteable;

import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;



public interface IReadWriteable extends IClass {
	
	public boolean read(DataInput input);
	
	public boolean write(DataOutput output);
}
