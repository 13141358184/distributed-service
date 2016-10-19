package com.xiaoheiwu.service.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.xiaoheiwu.service.transport.transport.TransportFactory;

import junit.framework.TestCase;

public class ServerTransportTest extends TestCase{

	public void testServerTransport(){
		IServerTransport serverTransprot=TransportFactory.getInstance().openServerTransport();
		serverTransprot.bind(8087);
		System.out.println("start server "+8087);
		while(true){
			ITransport transport=serverTransprot.accept();
			String result=readTransport(transport);
			System.out.println(result);
		}
	}

	private String readTransport(ITransport transport) {
		ByteBuffer dst=ByteBuffer.allocate(1000000);
		byte[] result=new byte[0];
		try {
			while(true){
				transport.read(dst);
				if(dst.limit()!=dst.capacity()){
					System.out.println(new String(result));
					result=new byte[0];
					continue;
				}
				dst.flip();
				
				byte[] value=new byte[dst.limit()];
				
			
				dst.get(value);
				result=copy(value,result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String(result);
	}

	private byte[] copy(byte[] value, byte[] result) {
		byte[] newBytes=new byte[value.length+result.length];
		int i=0;
		for(;i<result.length;i++){
			byte v=result[i];
			newBytes[i]=v;
		}
		for(int j=0;j<value.length;j++){
			newBytes[i]=value[j];
			i++;
		}
		return newBytes;
	}
}
