package com.xiaoheiwu.service.server.service.impl;

import com.xiaoheiwu.service.common.command.ICommand;

public class EchoCommand implements ICommand{

	@Override
	public String executeCommand(String... parameters) {
		int count=0;
		if(parameters==null||parameters.length!=1){
			if(parameters!=null)count=parameters.length;
			throw new RuntimeException("你的参数个数和期望的不一致，期望一个，实际上为"+count);
		}
		return parameters[0];
	}

	@Override
	public String getCommandName() {
		return "echo";
	}

	

}
