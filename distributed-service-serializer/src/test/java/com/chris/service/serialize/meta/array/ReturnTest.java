package com.chris.service.serialize.meta.array;

import junit.framework.TestCase;

import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;

public class ReturnTest extends TestCase{
	private ISerializer serializer=new MetaSerializer();
	public void testReturn(){
		Return return1=new Return();
		return1.setObject("hello yuanxiaodong");
		DataOutput output = serializer.serialize(return1);
		Return value=(Return)serializer.deserialize(new ByteDataInput(output.getData()));
		System.out.println(value.getObject());
	}
}
