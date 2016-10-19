package com.xiaoheiwu.service.common.command;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements ICommand{
	private String name;
	public Command(String name ){
		this.name=name;
	}
	@Override
	public String getCommandName() {
		return name;
	}
	
}
