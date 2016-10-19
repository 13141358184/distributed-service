package com.xiaoheiwu.service.balance.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;





import java.util.Set;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.balance.BalanceFactory;
import com.xiaoheiwu.service.balance.BalanceType;
import com.xiaoheiwu.service.balance.IBalance;
import com.xiaoheiwu.service.balance.IBalanceManager;
import com.xiaoheiwu.service.balance.IAccessFilter;
import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
@Service
@ServiceDefault(IBalanceManager.class)
public class BalanceManager implements IBalanceManager {
	private Map<String, IBalance> balances=new HashMap<String, IBalance>();
	private Set<String> localIps=new HashSet<>();
	private BalanceType defaultBalanceType=BalanceType.POLLING;
	@Override
	public <T> void addBalance(String name, List<T> elements) {
		this.addBalance(name, elements, null, defaultBalanceType);
	}
	@Override
	public <T> void addBalance(String name, List<T> elements,BalanceType balanceType) {
		this.addBalance(name, elements, null, balanceType);
	}

	@Override
	public <T> void addBalance(String name, List<T> balanceElements,
			IAccessFilter<T> processor,BalanceType balanceType) {
		IBalance<T> b=balances.get(name);
		List<T> elements=this.filterElements(balanceElements, processor);
		if(b!=null&&balanceType==b.getType()){
			b.setElement(elements);
			b.setProcessor(processor);
			return;
		}
		IBalance<T> balance=createBalance(elements, processor, balanceType);
		this.addBalance(name, balance);
	}

	
	@Override
	public <T> T doBalance(String name) {
		IBalance<T> balance=getBalance(name);
		return balance.doBalance();
	}

	@Override
	public <T> IBalance<T> getBalance(String name){
		return balances.get(name);
	}

	@Override
	public <T> void addBalance(String name, IBalance<T> balance) {
		IBalance<T> b=balances.get(name);
		if(b!=null){
			balance.synchronize(b);
		}
		balances.put(name, balance);
	}
	protected <T> List<T> filterElements(List<T> balanceElements,IAccessFilter<T> processor){
		if(processor==null)return balanceElements;
		List<T> list=new ArrayList<T>();
		for(T element:balanceElements){
			if(processor.enable(element)){
				list.add(element);
			}
		}
		return list;
	}
	protected <T> IBalance<T> createBalance(List<T> elements,
			IAccessFilter<T> processor,BalanceType balanceType){
		return BalanceFactory.createBalance(elements, processor, balanceType);
	}
	@Override
	public void setDefaultBalance(BalanceType balanceType){
		this.defaultBalanceType = balanceType;
	}
	
	@Override
	public <T> T doBalance(String name, IAccessFilter<T> filter) {
		IBalance<T> balance=getBalance(name);
		return balance.doBalance(filter);
	}

}
