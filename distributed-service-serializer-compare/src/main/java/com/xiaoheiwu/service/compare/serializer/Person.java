package com.xiaoheiwu.service.compare.serializer;

import java.util.List;

import com.xiaoheiwu.service.serializer.annotation.Required;
import com.xiaoheiwu.service.serializer.annotation.Transient;

public class Person {
	private String name;
	private int age;
	private String address;
	private long telephone;
	private List<Person> children;
	private Home home;

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

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("name="+name+",age="+age+",address="+address+",telephone="+telephone+",children="+getChildrenString()+",home="+home);
		return sb.toString();
	}

	private String getChildrenString() {
		StringBuilder sb=new StringBuilder("[");
		if(this.children!=null){
			for(Person person:this.children){
				sb.append(person+";");
			}
		}
		
		sb.append("]");
		return sb.toString();
		
	}
}
