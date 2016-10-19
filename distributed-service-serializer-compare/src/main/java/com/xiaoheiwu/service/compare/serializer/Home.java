package com.xiaoheiwu.service.compare.serializer;

import com.xiaoheiwu.service.serializer.annotation.Transient;


public class Home {
	@Transient
	private Person person;
	private String name="home";
	public Person getPerson() {
		return person;
	}
	public String getName() {
		return name;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
