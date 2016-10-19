package com.xiaoheiwu.service.balance;

public interface IAccessFilter<T> {
	
	/**
	 * 对选定的机器进行判断，如果可以访问返回true，否则返回false
	 * @param t 待判定的元素，如serviceNode节点
	 * @param d 参数，给具体业务使用。
	 * @return 是否可用
	 */
	public boolean enable(T t);
	
	
}
