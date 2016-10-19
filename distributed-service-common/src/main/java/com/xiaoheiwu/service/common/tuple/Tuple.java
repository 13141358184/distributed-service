package com.xiaoheiwu.service.common.tuple;

/**
 * 支持两个数据的读写。适合需要返回两个返回值的情况使用
 * @author Chris
 *
 * @param <T> 泛型T
 * @param <M> 泛型M
 */
public class Tuple<T,M> {
	private T t;
	private M m;
	public Tuple(){
		this.t=t;
		this.m=m;
	}
	public T getT() {
		return t;
	}
	public M getM() {
		return m;
	}
	public void setT(T t) {
		this.t = t;
	}
	public void setM(M m) {
		this.m = m;
	}
	
}
