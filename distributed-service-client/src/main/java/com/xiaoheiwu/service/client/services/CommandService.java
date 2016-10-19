package com.xiaoheiwu.service.client.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xiaoheiwu.service.common.command.ICommand;
import com.xiaoheiwu.service.manager.ICommandService;
/**
 
 * @author Chris
 *
 */
public class CommandService implements ICommandService{

	private ICommandService commandService=ServiceFactory.getInstance().getService(ICommandService.class);
	@Override
	public String executeCommand(String command, List<String> parameters) {
		return commandService.executeCommand(command, parameters);
	}

	@Override
	public List<String> listCommand() {
		return commandService.listCommand();
	}

	@Override
	public void registe(ICommand command) {
		throw new RuntimeException("不支持这个方法");
	}
	
}
