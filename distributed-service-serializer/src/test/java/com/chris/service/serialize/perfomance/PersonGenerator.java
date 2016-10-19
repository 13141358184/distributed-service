package com.chris.service.serialize.perfomance;

import java.util.ArrayList;
import java.util.List;

public class PersonGenerator {
	
	public static Person createPerson(int i){
		Person person=createPersonNoChildren(i);
		List<Person> children=new ArrayList<>();
		children.add(createPersonNoChildren(0));
		children.add(createPersonNoChildren(1));
		children.add(createPersonNoChildren(2));
		person.setChildren(children);
		return person;
	}
	public static Person createPersonNoChildren(int i){
		Person person=new Person();
		person.setName("袁小栋"+i);
		person.setAge(33+i);
		person.setTelephone(13141358184l+i);
		person.setAddress("北京市昌平区回龙观龙华园"+i+"楼8单元3们506");
		return person;
	}
}
