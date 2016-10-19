package com.xiaoheiwu.service.server.handle;

import com.xiaoheiwu.service.manager.pipline.IPipline;

/**
 * 支持pipline,把所有对request的处理串联起来
 * @author Chris
 *
 */
public interface IPiplineRequestHandle extends IRequestHandle,IPipline<IRequestHandle>{
}
