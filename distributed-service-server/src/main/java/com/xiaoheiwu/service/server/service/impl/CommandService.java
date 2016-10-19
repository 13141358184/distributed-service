package com.xiaoheiwu.service.server.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.annotation.ServiceDefault;
import com.xiaoheiwu.service.common.command.ICommand;
import com.xiaoheiwu.service.manager.ICommandService;

@Service
@ServiceDefault(ICommandService.class)
public class CommandService extends ConcurrentHashMap<String, ICommand> implements ICommandService {
	public CommandService(){
		this.registe(new EchoCommand());
	}
	@Override
	public String executeCommand(String commandName, List<String> parameters) {
		ICommand command=this.get(commandName);
		if(command==null){
			throw new RuntimeException("没有这个命令");
		}
		String[] values=new String[parameters.size()];
		int i=0;
		for(String value:parameters){
			values[i++]=value;
		}
		String result =command.executeCommand(values);
		return result;
	}

	@Override
	public List<String> listCommand() {
		Iterator<String> it = this.keySet().iterator();
		List<String> commands=new ArrayList<String>();
		while(it.hasNext()){
			commands.add(it.next());
		}
		Collections.sort(commands);
		return commands;
	}
	@Override
	public void registe(ICommand command) {
		this.putIfAbsent(command.getCommandName(), command);
	}

}
