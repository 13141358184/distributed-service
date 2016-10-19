package com.xiaoheiwu.service.compare.serializer;

import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;

public class SerializerCompare<T> implements ISerializerCompare<T>{
	private ISerializer serializer;
	private int count;
	private ICreator<T> creator;
	private long time;
	private int size;
	public SerializerCompare(ISerializer serializer,int count, ICreator<T> creator){
		this.serializer=serializer;
		this.count=count;
		this.creator=creator;
	}
	@Override
	public long getSerializeAndDeserializerTime() {
		return time;
	}

	@Override
	public int getSerializeAndDeserializerSize() {
		return size;
	}

	protected long executeSerializeAndDeserializer(T t) {
		long start=System.currentTimeMillis();
		DataOutput out = serializer.serialize(t);
		serializer.deserialize(new ByteDataInput(out.getData()));
		return (System.currentTimeMillis()-start)*1000*1000;
	}


	protected int executeSerialize(T t) {
		DataOutput out = serializer.serialize(t);
		return out.getData().length;
	}
	@Override
	public void run() {
		long sumTime=0;
		int sumSize=0;
		for(int i=0;i<count;i++){
			T t=creator.create(i);
			long wasteTime=executeSerializeAndDeserializer(t);
			int size=executeSerialize(t);
			sumTime+=wasteTime;
			sumSize+=size;
			
		}
		this.time=sumTime/count;
		this.size=sumSize/count;
	}
	@Override
	public String toString() {
		return serializer.getClass().getSimpleName()+"{time："+this.time+"，size:"+this.size+"}";
	}
	@Override
	public <T> T getSerializeAndDeserializer() {
		DataOutput out = serializer.serialize(this.creator.create(0));
		return (T)serializer.deserialize(new ByteDataInput(out.getData()));
	}
}
