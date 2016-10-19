package com.xiaoheiwu.service.common.component;

import java.net.URL;
import java.util.List;

/**
 * 发现spring配置文件
 * @author Chris
 *
 */
public interface IComponentScane {

	List<URL> findComponentConfigure();
}
