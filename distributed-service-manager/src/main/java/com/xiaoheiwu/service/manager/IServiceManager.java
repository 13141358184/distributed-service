package com.xiaoheiwu.service.manager;

import java.util.List;



public interface IServiceManager {
	
	/**
	 * 
	 * 获取一个服务的所有可用节点
	 * @param name 服务名称
	 * @param enable 服务是否可用
	 * @return 一个服务节点
	 */
	public IServiceNode getSeviceNode(String serviceName,String identity,boolean enable) ;
	
	/**
	 * 获取一个服务的所有可用节点
	 * @param name 服务名称
	 * @return 可用节点
	 */
	public IServiceNode getSeviceNode(String serviceName,String identity);
	/**
	 * 获取一个服务的所有可用节点
	 * @param name 服务名称
	 * @return 可用节点
	 */
	public List<IServiceNode> getSeviceNodes(String serviceName);
	
	
	/**
	 * 获取一个服务的所有可用节点,同时监听节点数据的变化
	 * @param name 服务名称
	 * @return 可用节点
	 */
	public List<IServiceNode> getSeviceNodes(String serviceName, boolean listern);
	
	/**
	 * 获取一个服务的配置信息
	 * @param name 服务名称
	 * @return
	 */
	public IService getService(String serviceName);
	
	/**
	 * 把在node中name服务暂停
	 * @param name 服务名称
	 * @param node 节点
	 */
	public boolean disableServiceNode(String serviceName, IServiceNode node);
	
	/**
	 * 把在node中name服务启动
	 * @param name 服务名称
	 * @param node 节点
	 */
	public boolean enableServiceNode(String serviceName, IServiceNode node);
	
	
	/**
	 * 发布一个服务
	 * @param service 服务对象
	 * @param serverNode 发布服务的节点
	 * @return
	 */
	public IServiceNode publishServiceNode(IService service,IServiceNode serverNode);
	
	/**
	 * 发布一个服务
	 * @param service 服务对象
	 * @param serverNode 发布服务的节点
	 * @return
	 */
	public IServiceNode publishServiceNode(IService service,IServiceNode serverNode, boolean enable);
	
	/**
	 * 删除整个服务，包括所有节点
	 * @param service 服务对象
	 * @param serverNode 发布服务的节点
	 * @return
	 */
	public void deleteService(String serviceName);
	
	/**
	 * 批量更新节点，会先删除旧的节点，重新发布新的节点
	 * @param name
	 * @param nodes
	 */
	public void rePublishAllServiceNode(String serviceName, List<IServiceNode> nodes);
	
	
	/**
	 * 监听服务的配置变化
	 * @param service
	 */
	public void listernService(IService service);
	
	/**
	 * 监听服务节点的配置变化
	 * @param service
	 */
	public void listernServiceNode(IServiceNode serviceNode);
	
	
	
}
