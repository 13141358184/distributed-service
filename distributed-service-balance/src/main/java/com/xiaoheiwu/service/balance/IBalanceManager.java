package com.xiaoheiwu.service.balance;

import java.util.List;
/**
 * 管理负载均衡
 * @author Chris
 *
 */
public interface IBalanceManager {
	/**
	 * 增加一个负载均衡
	 * @param name 使用者自己决定
	 * @param elements 待均衡的数据
	 */
	public <T> void addBalance(String name, List<T> elements);
	
	/**
	 * 增加一个负载均衡
	 * @param name 使用者自己决定
	 * @param balance 负载均衡器
	 */
	public <T> void addBalance(String name, IBalance<T> balance);

	/**
	 * 增加一个负载均衡
	 * @param name 使用者自己决定
	 * @param elements 需要负载均衡的数据
	 * @param balanceType  需要负载均衡的数据
	 */
	public <T> void addBalance(String name, List<T> elements,BalanceType balanceType);
	
	/**
	 * 增加一个负载均衡
	 * @param name 使用者自己决定
	 * @param elements 需要负载均衡的数据
	 * @param processor 对负载均衡结果进行处理
	 * @param balanceType 需要负载均衡的数据
	 */
	public <T> void addBalance(String name, List<T> elements, IAccessFilter<T> processor,BalanceType balanceType);
	
	/**
	 * 进行负责均衡
	 * @param name 用户自定义，和add保持一致
	 * @return 负载均衡结果
	 */
	public <T> T doBalance(String name);
	
	/**
	 * 进行负责均衡
	 * @param name 用户自定义，和add保持一致
	 * @param filter 不会覆盖在创建blance注册的filter。只是增加了一个filter.这个filter更多的是从临时请求的角度进行
	 * @return 负载均衡结果
	 */
	public <T> T doBalance(String name, IAccessFilter<T> filter);
	
	/**
	 * 获取负载均衡器
	 * @param name 和add时一致
	 * @return 负载均衡器
	 */
	public <T> IBalance<T> getBalance(String name);
	
	/**
	 * 设置默认负载均衡器
	 * @param balanceType 负载均衡类型
	 */
	public void setDefaultBalance(BalanceType balanceType);
	
}
