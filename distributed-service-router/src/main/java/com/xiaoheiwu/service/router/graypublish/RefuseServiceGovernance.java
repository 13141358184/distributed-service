package com.xiaoheiwu.service.router.graypublish;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;

public class RefuseServiceGovernance extends AbstractGrayPublishServiceeGonvernance{

	@Override
	protected boolean executeCommand(String[] paramters, IServiceNode node) {
		String ip=node.getIp();
		boolean match=doMatch(ip, paramters);
		return !match;
	}
	
	public static String getRefuseConfigure(String ...ips){
		return ServiceConfigureKey.GRAY_PUBLISHI_REFUSE+":"+getValue(ips);
	}

	@Override
	public ServiceConfigureKey getConfigureKey() {
		return ServiceConfigureKey.GRAY_PUBLISHI_REFUSE;
	}

}
