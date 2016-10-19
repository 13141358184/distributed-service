package com.xiaoheiwu.service.common.IdGenerator;

/**
 * 生成自增的id，当id即将达到上限的时候归0
 * @author Chris
 *
 */
public interface IServiceGenerator {
	
	public long getServiceCallId();
}
