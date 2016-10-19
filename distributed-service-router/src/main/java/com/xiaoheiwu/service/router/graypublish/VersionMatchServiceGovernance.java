package com.xiaoheiwu.service.router.graypublish;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.version.VersionUtil;

/**
 * GRAY_PUBLISH_VERSION_MATCH:1.0.1;1.0.2;1.1.*
 * @author Chris
 *
 */
public class VersionMatchServiceGovernance extends AbstractGrayPublishServiceeGonvernance{
	@Override
	public ServiceConfigureKey getConfigureKey() {
		return ServiceConfigureKey.GRAY_PUBLISH_VERSION_MATCH;
	}

	@Override
	protected boolean executeCommand(String[] paramters, IServiceNode node) {
		String serviceName=node.getService().getName();
		String version=getVersion(serviceName);
		if(version==null)return true;
		boolean match=doMatch(version, paramters);
		return match;
	}

	private String getVersion(String serviceName) {
		return VersionUtil.getVersion(serviceName);
	}

}
