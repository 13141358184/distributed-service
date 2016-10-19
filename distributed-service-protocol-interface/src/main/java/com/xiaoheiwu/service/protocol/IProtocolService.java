package com.xiaoheiwu.service.protocol;

public interface IProtocolService {

	public byte[] doCall(IServiceRequest call);
	
	public IServiceResponse doReturn(byte[] data);
	
	public IServiceRequest doCall(byte[] data);
	
	public byte[] doReturn(IServiceResponse value);
	
	public byte getIndex();
	
	public boolean isBeatHeart(byte[] data);
	
	public byte[] createBeatHeart();
}
