package com.xiaoheiwu.service.router;

import com.xiaoheiwu.service.balance.IAccessFilter;
import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.manager.IServiceNode;
import com.xiaoheiwu.service.manager.governance.IServiceGovernance;
import com.xiaoheiwu.service.manager.governance.ServiceGovernanceContainer;

public class RouterGovernanceContainer extends ServiceGovernanceContainer<IServiceGovernance<IServiceNode>, IServiceNode> implements IAccessFilter<IServiceNode>{
	private static final RouterGovernanceContainer instance=new RouterGovernanceContainer();
	private RouterGovernanceContainer(){
		
	}
	public static RouterGovernanceContainer getInstance(){
		return instance;
	}
	@Override
	public boolean enable(IServiceNode request) {
		return this.handle(request);
	}
	

}
