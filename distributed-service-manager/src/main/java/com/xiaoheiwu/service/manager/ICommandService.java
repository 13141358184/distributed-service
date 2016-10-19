package com.xiaoheiwu.service.manager;

import java.util.List;

import com.xiaoheiwu.service.annotation.Service;
import com.xiaoheiwu.service.common.annotation.*;
import com.xiaoheiwu.service.common.command.ICommand;

@Service("CommandService")
public interface ICommandService {

	public String executeCommand(String command, List<String> parameters);
	
	
	public List<String> listCommand();
	
	
	
	public void registe(ICommand command);
	
}
