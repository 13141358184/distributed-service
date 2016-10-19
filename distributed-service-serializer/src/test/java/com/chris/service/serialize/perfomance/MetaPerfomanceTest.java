package com.chris.service.serialize.perfomance;

import junit.framework.TestCase;

import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;

public class MetaPerfomanceTest extends TestCase{
	private ISerializer serializer=new MetaSerializer();
	public void testPersonTime() throws InterruptedException{
		long sum=0;
		int count=100000;
		for(int i=0;i<10;i++){
			long time =serializeString(count,PersonGenerator.createPerson(i));
			sum+=time;
		}
		System.out.println("person avg time is "+sum/10/count);
	}
	public void testPersonSize() throws InterruptedException{
		long sum=0;
		int count=100000;
		for(int i=0;i<10;i++){
			int size =serializeStringSize(count,PersonGenerator.createPerson(i));
			sum+=size;
		}
		System.out.println("person avg size is "+sum/10/count);
	}
	public void testStringTime() throws InterruptedException{
		long sum=0;
		int count=1000000;
		for(int i=0;i<10;i++){
			long time =serializeString(count,"你好，world!");
			sum+=time;
		}
		System.out.println("string avg time is "+sum/10/count);
	}
	public void testStringSize() throws InterruptedException{
		long sum=0;
		int count=1000000;
		for(int i=0;i<10;i++){
			int size =serializeStringSize(count,"你好，world!");
			sum+=size;
		}
		System.out.println("string avg size is "+sum/10/count);
	}
	public int  serializeStringSize(int count,Object object) throws InterruptedException{
		int sum=0;
		long start=System.currentTimeMillis();
		
		for(int i=0;i<count;i++){
			DataOutput out=serializer.serialize(object);
			sum+=out.getData().length;
		}
		return sum;

	}
	public long  serializeString(int count,Object object) throws InterruptedException{
		
		long start=System.currentTimeMillis();
		
		for(int i=0;i<count;i++){
			DataOutput out=serializer.serialize(object);
			serializer.deserialize(new ByteDataInput(out.getData()));
		}
		return (System.currentTimeMillis()-start);

	}
}
