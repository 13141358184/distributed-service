package com.xiaoheiwu.service.common.classloader.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.xiaoheiwu.service.common.classloader.IClassLoader;
import com.xiaoheiwu.service.common.classloader.IRepository;

public class CommonClassLoader extends URLClassLoader implements IClassLoader{
	

	public CommonClassLoader(IRepository repository, ClassLoader parent) {
		super(getUrls(repository), parent);

	}

	private static URL[] getUrls(IRepository repository) {
		if (repository == null || repository.getRepository() == null)
			return new URL[0];
		List<URL> list = repository.getRepository();
		URL[] urls = new URL[list.size()];
		int i = 0;
		for (URL url : list) {
			urls[i] = url;
			i++;
		}
		return urls;
	}

	public static void main(String[] args) {
		URL url = CommonClassLoader.class.getResource("/");
		System.out.println(url.getPath());
	}

}
