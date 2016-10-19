package com.xiaoheiwu.service.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ZKClientTest 
    extends TestCase{
  public void testCreateNode() throws Exception{
	  System.out.println("testCreateNode");
	  ZKClient.create("/HelloAPI/enable/192.168.0.1:8087", CreateMode.EPHEMERAL, "");
  }
  public void testGetChildren() throws Exception{
	  List<String> list=ZKClient.getChildren("/HelloAPI/enable", false);
	  System.out.println(list.size());
	  for(String tmp:list){
		  System.out.println(tmp);
	  }
  }

}
