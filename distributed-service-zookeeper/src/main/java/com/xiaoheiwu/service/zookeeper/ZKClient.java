package com.xiaoheiwu.service.zookeeper;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.common.configure.IConfigure;
import com.xiaoheiwu.service.common.configure.impl.AutoPropertiesLoaderConfigure;
import com.xiaoheiwu.service.common.util.ClassFinderUtil;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;


public class ZKClient {
	public static final  CreateMode EPHEMERAL=CreateMode.EPHEMERAL;
	public static final  CreateMode EPHEMERAL_SEQUENTIAL=CreateMode.EPHEMERAL_SEQUENTIAL;
	public static final  CreateMode PERSISTENT=CreateMode.PERSISTENT;
	public static final  CreateMode PERSISTENT_SEQUENTIAL=CreateMode.PERSISTENT_SEQUENTIAL;
	public static Logger logger = Logger.getLogger(ZKClient.class);
    private static CuratorFramework client;
    private static ISerializer serializer=ComponentProvider.getInstance(ISerializer.class);
	static{
		client=getClient();
	}
    public static CuratorFramework getClient() {
        if (client == null) {
            synchronized (ZKClient.class) {
                if (client == null) {
                	IConfigure configure=new AutoPropertiesLoaderConfigure(ClassFinderUtil.getPackage(ZKClient.class));
                	String hosts=configure.getStringValue("zk_hosts");
                	 if (hosts == null) {
                         throw new RuntimeException("Need conf for zookeeper hosts.");
                     }
                	 String namespace=configure.getStringValue("zk_root");
                	 client = CuratorFrameworkFactory
                             .builder()
                             .connectString(hosts)
                             .namespace(namespace)
                             .retryPolicy(
                                     new RetryNTimes(Integer.MAX_VALUE, 5 * 60 * 1000))
                             .connectionTimeoutMs(5000).build();
                    
                    }
                    client.start();
                }
            }
        
        return client;
    }
	public static void close(){
		getClient().close();
	}
	public static <T> void create(String path,CreateMode mode, T t) throws Exception{
		byte[] value=serializer.serialize(t).getData();
		getClient().create().creatingParentsIfNeeded().withMode(mode).forPath(path, value);
	}
	public static <T> T get(String path,boolean usingWatcher) throws Exception{
		byte[] value;
		if(usingWatcher){
			value=getClient().getData().usingWatcher(ZKWatcher.getInstance()).forPath(path);
		}else{
			value=getClient().getData().forPath(path);
		}
		
		return (T)serializer.deserialize(new ByteDataInput(value));
	}
	public static void listern(String path){
		try {
			get(path, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean exist(String path, boolean useWatcher) throws Exception{
		Stat result = getStat(path,useWatcher);
		return result==null?false:true;
	}
	public static Stat getStat(String path, boolean usingWatcher) throws Exception{
		Stat result ;
		if(usingWatcher){
			result= getClient().checkExists().usingWatcher(ZKWatcher.getInstance()).forPath(path);
		}else{
			result= getClient().checkExists().forPath(path);
		}
		return result;
	}
	
	public static <T>  void set(String path,T t) throws Exception{
		DataOutput output=serializer.serialize(t);
		getClient().setData().forPath(path, output.getData());
	}
	
	public static List<String> getChildren(String path, boolean useWatcher) throws Exception{
		
		List<String> children;
		if(useWatcher){
			children= getClient().getChildren().usingWatcher(ZKWatcher.getInstance()).forPath(path);
		}else{
			children=getClient().getChildren().forPath(path);
		}
		return children;
	}
	public static void delete(String path) throws Exception{
		getClient().delete().forPath(path);
	}
	public static void main(String[] args) throws Exception {
	
	}
}
