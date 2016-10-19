package com.xiaoheiwu.service.balance;

import java.util.List;

/**
 * 对于一组数据，进行负载均衡选择
 * @author Chris
 *
 * @param <T>
 */
public interface IBalance<T> {

	/**
	 * 
	 * @return 返回负载均衡结果
	 */
	public <D> T doBalance();
	
	/**
	 * 
	 * @param processor 判断选择的结果是否符合条件，如果不符合条件，则继续选择
	 * @return 选择负载均衡结果
	 */
	public <D> T doBalance(IAccessFilter<T> processor);
	
	/**
	 * 设置处理器，用来对均衡结果进行处理
	 * @param processor
	 */
	public void setProcessor(IAccessFilter<T>  processor);
	
	/**
	 * 设置待负载均衡的数据
	 * @param elements
	 */
	public void setElement(List<T> elements);
	
	/**
	 * 增加负载均衡的数据
	 * @param element
	 */
	public void addElement(T element);
	
	/**
	 * 移除负载均衡元素
	 * @param element
	 */
	public void removeElement(T element);
	
	/**
	 * 如果同步新的负载均衡器的内部数据
	 * @param b
	 */
	public void synchronize(IBalance<T> b);
	
	/**
	 * 
	 * @return 返回负载均衡类型
	 */
	public BalanceType getType();
	
	public List<T> getElements();
}
