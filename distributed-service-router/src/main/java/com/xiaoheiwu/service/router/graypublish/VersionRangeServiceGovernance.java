package com.xiaoheiwu.service.router.graypublish;

import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.configure.ServiceConfigureKey;
import com.xiaoheiwu.service.manager.version.VersionUtil;

/**
 * GRAY_PUBLISH_VERSION_RANGE:1.0.1,1.0.8;
 * @author Chris
 *
 */
public class VersionRangeServiceGovernance extends VersionMatchServiceGovernance{
	@Override
	public ServiceConfigureKey getConfigureKey() {
		return ServiceConfigureKey.GRAY_PUBLISH_VERSION_RANGE;
	}



	private String getVersion(String serviceName) {
		return VersionUtil.getVersion(serviceName);
	}

	@Override
	protected boolean doMatch(String version, String... paramters) {
		for(String paramter: paramters){
			boolean match=matchRange(version, paramter);
			if(match)return match;
		}
		return false;
	}

	protected boolean matchRange(String version, String paramter) {
		String[] values=paramter.split(",");
		if(values.length!=2)return super.doMatch(version, paramter);
		if(version.compareTo(values[0])>=0&&version.compareTo(values[1])<=0){
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		VersionRangeServiceGovernance governance=new VersionRangeServiceGovernance();
		boolean match=governance.matchRange("1.0.1", "1.0.4");
		System.out.println(match);
	}
}
