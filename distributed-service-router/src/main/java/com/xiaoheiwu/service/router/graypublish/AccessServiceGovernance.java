package com.xiaoheiwu.service.router.graypublish;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.governance.AbstractServiceGovernance;
import com.xiaoheiwu.service.manager.governance.IRequestAccess;
import com.xiaoheiwu.service.manager.governance.imp.AbstractRequestAccess;

public class AccessServiceGovernance extends AbstractGrayPublishServiceeGonvernance{

	@Override
	public ServiceConfigureKey getConfigureKey() {
		return ServiceConfigureKey.GRAY_PUBLISHI_ACCESS;
	}

	
	public static String getAccessConfigure(String ...ips){
		return ServiceConfigureKey.GRAY_PUBLISHI_ACCESS+":"+getValue(ips);
	}
	
	protected boolean executeCommand(String[] paramters,
			IServiceNode node) {
		String ip=node.getIp();
		boolean match=doMatch(ip, paramters);
		return match;
	}
}
