package com.xiaoheiwu.service.container.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.sun.org.apache.bcel.internal.generic.IADD;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.component.Components;
import com.xiaoheiwu.service.common.component.IComponents;

@Service("wbsd")
public class Person {
	private Address address;
	
	public void setAddress(String address){
		this.address=new Address();
	}
	public void print(){
		System.out.println(this.address);
	}
	public static void main(String[] args) {
		IComponents components=Components.getInstance();
		components.start();
		Person person=(Person) components.getBean("wbsd");
		System.out.println(person);
		Address address=(Address) components.getBean(IAdress.class);
		System.out.println(address);
		person.print();
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
