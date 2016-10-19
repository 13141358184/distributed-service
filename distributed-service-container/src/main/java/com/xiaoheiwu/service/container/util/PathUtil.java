package com.xiaoheiwu.service.container.util;

import java.io.File;

public class PathUtil {
	
	public static String getPath(String dir,String fileName){
		if(dir.endsWith(File.separator)){
			dir=dir.substring(0,dir.length()-1);
		}
		return dir+File.separator+fileName;
	}
}
