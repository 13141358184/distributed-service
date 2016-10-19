package com.xiaoheiwu.service.compare.serializer;


public interface ISerializerCompare<T> extends Runnable{
	
	/**
	 * 
	 * @return 压缩和解压缩需要花费的时间，单位为ns。1s=1000*1000*1000ns
	 */
	public long getSerializeAndDeserializerTime();
	
	/**
	 * 
	 * @return 压缩后大小，单位为byte
	 */
	public int getSerializeAndDeserializerSize();
	
	
	public <T> T getSerializeAndDeserializer();
}
