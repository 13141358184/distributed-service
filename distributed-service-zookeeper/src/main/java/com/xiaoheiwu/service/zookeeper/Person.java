package com.xiaoheiwu.service.zookeeper;

public class Person {
	private String name="sb";
	private int age=30;
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("name:"+name+";age="+age);
		return sb.toString();
	}
	
}
