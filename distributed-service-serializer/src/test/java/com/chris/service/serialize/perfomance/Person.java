package com.chris.service.serialize.perfomance;

import java.util.List;

public class Person {
	private String name;
	private int age;
	private String address;
	private long telephone;
	private List<Person> children;
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getAddress() {
		return address;
	}
	public long getTelephone() {
		return telephone;
	}
	public List<Person> getChildren() {
		return children;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setTelephone(long telephone) {
		this.telephone = telephone;
	}
	public void setChildren(List<Person> children) {
		this.children = children;
	}
	
}
