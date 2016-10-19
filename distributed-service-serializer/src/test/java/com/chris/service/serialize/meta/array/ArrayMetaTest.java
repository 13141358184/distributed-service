package com.chris.service.serialize.meta.array;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;

import junit.framework.TestCase;
import sun.management.counter.Units;

public class ArrayMetaTest extends TestCase{

	ISerializer serializer=ComponentProvider.getInstance(MetaSerializer.class);
	public void testArrayMeta(){
//		Object[]  parameters=new Object[3];
//		parameters[0]="134";
//		parameters[1]=97;
//		parameters[2]="wangbadan";
//		DataOutput output = serializer.serialize(parameters);
//		Object[] tmp=(Object[])serializer.deserialize(new ByteDataInput(output.getData()));
//		System.out.println(tmp);
		System.out.println(serializer);
	}
}
