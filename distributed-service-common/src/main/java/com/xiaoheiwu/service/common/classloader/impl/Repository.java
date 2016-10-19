package com.xiaoheiwu.service.common.classloader.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.xiaoheiwu.service.common.classloader.IRepository;

public class Repository implements IRepository{
	private List<URL> urlList=new ArrayList<URL>();
	@Override
	public void addJar(String... jarNames) {
		if(jarNames==null)return ;
		for(String jarName: jarNames){
			URL url=createJarURL(jarName);
			urlList.add(url);
		}
	}

	

	@Override
	public void addJarDir(String... dirNames) {
		if(dirNames==null)return ;
		for(String name: dirNames){
			List<URL> urls=createJarDirURL(name);
			this.urlList.addAll(urls);
		}
	}

	@Override
	public void addFile(String... fileNames) {
		if(fileNames==null)return ;
		for(String name: fileNames){
			URL url=createFileURL(name);
			urlList.add(url);
		}
	}





	@Override
	public void addPath(String path) {
		StringTokenizer st=new StringTokenizer(path, ";");
		while(st.hasMoreTokens()){
			String value=st.nextToken();
			if(value.endsWith("*.jar")){
				this.addJarDir(value);
			}else if(value.endsWith(".jar")){
				this.addJar(value);
			}else{
				this.addFile(value);
			}
		}
	}
	private URL createJarURL(String jarName) {
		if(!jarName.endsWith(".jar")){
			return null;
		}
		if(jarName.endsWith("*.jar")){
			return null;
		}
		try {
			URL url=new URL("jar:file:"+jarName+"!/");
			return url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	private URL createFileURL(String name) {
		if(name.endsWith(".jar")){
			return null;
		}
		if(name.endsWith("*.jar")){
			return null;
		}
		try {
			URL url=new URL("file:"+name);
			return url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	private List<URL> createJarDirURL(String name) {
		List<URL> urls=new ArrayList<URL>();
		String path=name.replace("*.jar", "");
		File dir=new File(path);
		if(dir.isFile()){
			return null;
		}
		File[] files=dir.listFiles();
		if(files==null)return new ArrayList<URL>();
		for(File file:files){
			if(file.isDirectory()){
				List<URL> tmp=createJarDirURL(file.getAbsolutePath()+"*.jar");
				urls.addAll(tmp);
			}
			if(file.getName().endsWith(".jar")){
				URL url=createJarURL(file.getAbsolutePath());
				if(url!=null)urls.add(url);
			}
		}
		return urls;
	}



	@Override
	public List<URL> getRepository() {
		return this.urlList;
	}



	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(URL url:this.urlList){
			sb.append(url.getPath()+"\r\n");
		}
		return sb.toString();
	}
	
}
