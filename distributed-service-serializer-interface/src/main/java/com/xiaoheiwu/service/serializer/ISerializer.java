package com.xiaoheiwu.service.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;


public interface ISerializer {

	/**
	 * 判断一个对象是否可以被序列化
	 * @param object 待序列化的对象
	 * @return true：可以被序列化；false：不能被序列化
	 */
	public boolean isSerializable(Object object);
	
	/**
	 * 把一个对象序列化为字节数组
	 * @param object
	 * @return
	 */
	public DataOutput serialize(Object object) throws NotSupportSerializerException;
	
	/**
	 * 把一个字节数组反序列化为一个对象
	 * @param objectBytes 待反序列化的字节数组
	 * @return 反序列化后的对象
	 * @throws NotSupportSerializer 如果序列化程序不能反序列化此对象，则抛出异常
	 */
	public Object deserialize(DataInput input)throws NotSupportSerializerException;
	
	/**
	 * 把一个对象序列化后写入流中，此方法不负责关闭out
	 * @param object 待序列化的对象
	 * @param out 序列化后待写入的流
	 * @throws NotSupportSerializer 如果对象不能被序列化程序识别，则抛出此异常
	 * @throws IOException 在写入流时出现io异常，则抛出此错误
	 */
	public void writeObject(Object object, OutputStream out)throws NotSupportSerializerException,IOException;
	
	/**
	 * 把一个对象序从字节数组中反序列化出来
	 * @param object 待序列化的对象
	 * @param out 序列化后待写入的流
	 * @throws NotSupportSerializer 如果对象不能被序列化程序识别，则抛出此异常
	 * @throws IOException 在写入流时出现io异常，则抛出此错误
	 */
	/**
	 * 
	 * @param in 从此流中读取待序列化的对象,此方法不负责关闭in
	 * @return 反序列化后的对象
	 * @throws NotSupportSerializer 如果对象不能被序列化程序识别，则抛出此异常
	 * @throws IOException 在写入流时出现io异常，则抛出此错误
	 */
	public Object readObject( InputStream in)throws NotSupportSerializerException,IOException;
	
	/**
	 * 在接口反射时使用，把子类还原成原始接口类
	 * @param clazz
	 * @return
	 */
	public Class getDataTypeClass(Class clazz);
	
}
