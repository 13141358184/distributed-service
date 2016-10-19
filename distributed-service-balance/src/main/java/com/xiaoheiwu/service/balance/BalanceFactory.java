package com.xiaoheiwu.service.balance;

import java.util.List;

import com.xiaoheiwu.service.balance.impl.PollingBalance;
/**
 * 根据负载均衡类型，创建对应的负载均衡器
 * @author Chris
 *
 */
public class BalanceFactory {
	
	public static <T> IBalance<T> createBalance(List<T> elements,
			IAccessFilter<T> processor,BalanceType balanceType){
		if(BalanceType.POLLING==balanceType){
			IBalance<T> balance=new PollingBalance<T>(elements,processor);
			return balance;
		}
		return null;
	}
}
