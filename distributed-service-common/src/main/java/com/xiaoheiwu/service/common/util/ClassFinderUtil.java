package com.xiaoheiwu.service.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class ClassFinderUtil {
	public static final String PROPERTIES_SUFFIX="properties";
	public static final String CLASS_SUFFIX="class";
	/**
	 * 通过包名获取所在的路径
	 * @param interfaceName
	 * @return
	 */
	public static String getPackage(String className){
		int index=className.lastIndexOf(".");
		return className.substring(0,index);
	}
	public static String getPackage(Class clazz){
		return getPackage(clazz.getName());
	}
	/** 
     *  
    * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的 
    * @param pageName 包名 
    * @return List<Class>    包下所有类 
    * @author LiYaoHua  
    * @date 2012-4-5 上午11:26:48 
     */  
    public static  List<Class> getClasses(String packageName, boolean loadSubPackage) throws ClassNotFoundException,IOException{  
    	List<Class> classes=new ArrayList<Class>();
    	List<ResourceFile> resoures=getResource(packageName, CLASS_SUFFIX, loadSubPackage);
    	for(ResourceFile file: resoures){
    		String resource=file.packageName+"."+file.file.getName();
    		String className=resource.substring(0,resource.length()-CLASS_SUFFIX.length()-1);
    		Class clazz=Class.forName(className);
    		classes.add(clazz);
    	}
    	return classes;
    }
    public static  List<Properties> getProperties(String packageName) throws IOException{
    	return getProperties(packageName, true);
    }
    
    public static  List<Properties> getProperties(String packageName, boolean loadSubPackage) throws IOException {
    	List<Properties> ps=new ArrayList<Properties>();
    	List<ResourceFile> resoures=getResource(packageName, PROPERTIES_SUFFIX, loadSubPackage);
    	for(ResourceFile file: resoures){
    		InputStream in=new FileInputStream(file.file);
    		Properties properties=new Properties();
    		properties.load(in);
    		ps.add(properties);
    	}
    	return ps;
    }
	private static String getFilePath(String resource,String suffix) {
		String filePath=resource.replace("."+suffix, "");
		filePath=filePath.replace(".",File.separator);
		filePath=filePath+"."+suffix;
		return filePath;
	}
	/** 
     *  
    * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的 
    * @param pageName 包名 
    * @return List<Class>    包下所有类 
    * @author LiYaoHua  
    * @date 2012-4-5 上午11:26:48 
     */  
    public static  List<ResourceFile> getResource(String packageName, String suffix, boolean loadSubPackage) throws IOException{  
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();  
        String path = packageName.replace(".", "/");  
        Enumeration<URL> resources = classLoader.getResources(path);  
        List<File> dirs = new ArrayList<File>();  
        while(resources.hasMoreElements()){  
            URL resource = resources.nextElement();  
            dirs.add(new File(resource.getFile()));  
        }  
        ArrayList<ResourceFile> resourceNames = new ArrayList<ResourceFile>();  
        for(File directory:dirs){  
        	resourceNames.addAll(findResource(directory, packageName, suffix, loadSubPackage));  
        }  
        return resourceNames;  
    }  
      
    public static  List<ResourceFile> findResource(File directory, String packageName, String suffix, boolean loadSubPackage) {  
        List<ResourceFile> resourceNames = new ArrayList<ResourceFile>();  
        if(!directory.exists()){  
            return resourceNames;  
        }  
        File[] files = directory.listFiles();  
        for(File file:files){  
            if(file.isDirectory()&&loadSubPackage){  
                assert !file.getName().contains(".");  
                resourceNames.addAll(findResource(file, packageName+"."+file.getName(),suffix,loadSubPackage));  
            }else if(file.getName().endsWith("."+suffix)){  
            	resourceNames.add(new ResourceFile(file, packageName));  
            }  
        }  
        return resourceNames;  
    }
    public static class ResourceFile{
    	private String packageName;
    	private File file;
		public ResourceFile(File file, String packageName) {
			this.packageName=packageName;
			this.file=file;
		}
    	
    }
	public static boolean canInstance(Class class1) {
		int mod=class1.getModifiers();
		if(Modifier.isAbstract(mod))return false;
		if(Modifier.isInterface(mod))return false;
		if(Modifier.isPrivate(mod))return false;
		if(Modifier.isStatic(mod))return false;
		Constructor[] cs=class1.getConstructors();
		boolean success=false;
		for(Constructor c:cs){
			if(c.getGenericParameterTypes().length==0){
				success=true;
			}
		}
		if(!success)return false;
		return true;
	}
    public static void main(String[] args) throws ClassNotFoundException, IOException {
    	ClassFinderUtil.canInstance(ClassFinderUtil.class);
	}
    
}
