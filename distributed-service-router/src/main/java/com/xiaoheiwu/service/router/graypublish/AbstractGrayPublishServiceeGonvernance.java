package com.xiaoheiwu.service.router.graypublish;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.governance.AbstractServiceGovernance;
import com.xiaoheiwu.service.manager.governance.IRequestAccess;
import com.xiaoheiwu.service.manager.governance.imp.AbstractRequestAccess;

public abstract class AbstractGrayPublishServiceeGonvernance extends AbstractServiceGovernance<IRequestAccess<String,IServiceNode>,  IServiceNode>{

	@Override
	protected IRequestAccess<String, IServiceNode> newRequestAccess(String serviceName, IServiceNode node) {
		return new AbstractRequestAccess<IServiceNode>() {

			@Override
			public boolean access(IServiceNode serviceNode) {
				String value=getValue();
				
				return execute(value, serviceNode);
			}

			@Override
			public String getExceptionCode() {
				return getConfigureKey().name();
			}
		};
	}
	
	protected boolean execute(String value, IServiceNode node){
		if(value==null||"".equals(value))return true;
		String[] values=value.split(":");
		if(values.length==0)return true;
		String[] paramters=values[1].split(";");
		boolean enable=executeCommand(paramters, node);
		return enable;
	}
	protected abstract  boolean executeCommand(String[] paramters,
			IServiceNode node) ;
	
	protected boolean doMatch(String ip, String... paramters) {
		if(paramters==null)return true;
		for(String parameter: paramters){
			 Pattern pattern = Pattern.compile(parameter);  
			 Matcher matcher=pattern.matcher(ip);
			 if(matcher.matches())return true;
		}
		return false;
	}
	protected boolean doRearMatch(String ip, String[] paramters) {
		 for(String parameter: paramters){
			 if(ip.endsWith(parameter))return true;
		 }
		
		return false;
	}
	@Override
	protected String getSeviceName(IServiceNode request) {
		return request.getService().getName();
	}

	@Override
	protected void doRejectHandle(IServiceNode request) {
	}
	/**
	 *  把数组转格式化成字符串
	 * @param ips ip数组
	 * @return 格式字符创
	 */
	protected static String getValue(String ...ips){
		if(ips==null)return null;
		StringBuilder sb=new StringBuilder();
		boolean isFirst=true;
		for(String ip:ips){
			if(isFirst){
				isFirst=false;
			}else{
				sb.append(";");
			}
			sb.append(ip);
		}
		return sb.toString();
	}
}
