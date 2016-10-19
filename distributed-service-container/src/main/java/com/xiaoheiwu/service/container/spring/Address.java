package com.xiaoheiwu.service.container.spring;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class Address implements IAdress{
	private String address;
	public Address(){
		this.address="龙腾苑四区";
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
