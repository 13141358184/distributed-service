package com.xiaoheiwu.service.common.command;



/**
 * 提供一种命令处理器，由命令的实现这完成命令参数的语义解释和命令的执行
 * @author Chris
 *
 */
public interface ICommand {
	public String executeCommand(String... parameters);
	
	public String getCommandName();

}
