package com.xiaoheiwu.service.server.handle.service.limit;


import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.xiaoheiwu.service.protocol.ResponseCode;
import com.xiaoheiwu.service.server.protocol.IServerRequest;






public class RequestLimit implements IRequestLimit{
	private volatile TimeUnit timeUnit=TimeUnit.SECOND;
	private String configureInfo;
	private volatile int requestCount=-1;
	private volatile long lastClearTime=-1;
	private ConcurrentMap<String, AtomicInteger> time2Limit=new ConcurrentHashMap<String, AtomicInteger>();
	@Override
	public boolean setLimit(TimeUnit timeUnit, int requestCount) {
		this.timeUnit=timeUnit;
		this.requestCount=requestCount;
		return true;
	}
	
	@Override
	public boolean setValue(String limitInfo) {
		if(limitInfo==null)return false;
		this.configureInfo=limitInfo;
		String[] values=limitInfo.split(":");
		if(values.length!=2){
			return false;
		}
		TimeUnit unit=TimeUnit.getTimeUnit(values[0]);
		if(unit==null)return false;
		this.timeUnit=unit;
		try{
			int value=Integer.valueOf(values[1]);
			this.requestCount=value;
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean access(IServerRequest request) {
		if(requestCount==-1)return true;
		Date date=new Date();
		
		String limitKey=getLimitKey(timeUnit, date);
		AtomicInteger atomicInteger=time2Limit.get(limitKey);
		//如果是当前单位时间的第一个请求。则进行插入
		if(atomicInteger==null){
			atomicInteger=new AtomicInteger(0);
			AtomicInteger oldValue = time2Limit.putIfAbsent(limitKey, atomicInteger);
			if(oldValue!=null){
				atomicInteger=oldValue;
			}
		}
		int requestCount=atomicInteger.incrementAndGet();
		clearOldKey(limitKey);
		if(requestCount<this.requestCount)return true;
		return false;
	}
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	/**
	 * 清理过期的数据，默认每10分钟清理一次
	 * @param nowLimitKey
	 */
	private void clearOldKey(String nowLimitKey) {
		if(lastClearTime==-1){//如果没有清理过，设置当前时间为最后清理时间，10分钟后启动清理
			lastClearTime=System.currentTimeMillis();
			return;
		}
		int clearTime=60*1000*10;//10分钟清理一次
		if((System.currentTimeMillis()-lastClearTime)>clearTime){
			synchronized (this) {
				if((System.currentTimeMillis()-lastClearTime)>clearTime){
					
					lastClearTime=System.currentTimeMillis();
					Iterator<String> it = time2Limit.keySet().iterator();
					while(it.hasNext()){
						String key=it.next();
						if(!key.equals(nowLimitKey)){
							System.out.println("clear key:"+key+";value:"+time2Limit.get(key));
							time2Limit.remove(key);
							
						}
					}
				}
			}
			
		}
	}

	public int getRequestCount() {
		return requestCount;
	}
	/**
	 * 按限流的单位作为健值，当前有效的健值只有一个，其它键值采用延迟清理的方式
	 * @param timeUnit 单位
	 * @param date 访问时的时间
	 * @return
	 */
	private String getLimitKey(TimeUnit timeUnit, Date date){
		if(timeUnit==TimeUnit.SECOND){
			return getHour(date)+":"+getMinute(date)+":"+getSecond(date);
		}else if (timeUnit==TimeUnit.MINITUE){
			return getHour(date)+":"+getMinute(date);
		}else if(timeUnit==TimeUnit.HOUR){
			return getHour(date)+"";
		}
		return null;
	}

	private int getSecond(Date date){
		int second=date.getSeconds();
		return second;
	}
	private int getMinute(Date date){
		int minute=date.getMinutes();
		return minute;
	}
	private int getHour(Date date){
		int hour=date.getHours();
		return hour;
	}
	public static void main(String[] args) throws InterruptedException {
//		RequestLimit limit=new RequestLimit();
//		limit.setLimit(TimeUnit.SECOND, 1000);
//		while(true){
//			limit.access();
//		}
	}

	@Override
	public String getValue() {
		return configureInfo;
	}

	@Override
	public ResponseCode getExceptionCode() {
		return ResponseCode.LIMIT_EXCEPTION;
	}

}
