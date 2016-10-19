package com.xiaoheiwu.service.compare.serializer;

import com.xiaoheiwu.service.compare.serializer.kryo.KryoSerializer;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;

public class Startup {
	private int count=100000;
	private ICreator creator=new PersonCreator();
	public static void main(String[] args) {
		Startup  startup=new Startup();
		MetaSerializer metaSerializer=new MetaSerializer();
		startup.executeSerializerAndTest(metaSerializer);
		startup.executeSerializer(metaSerializer);
		startup.executeSerializer(new KryoSerializer());
	}
	public void executeSerializerAndTest(ISerializer serializer){
		ISerializerCompare<Person> compare=new SerializerCompare<>(serializer,1, creator);
		Person person=compare.getSerializeAndDeserializer();
		System.out.println(person.toString());
	}
	public void executeSerializer(ISerializer serializer){
		ISerializerCompare<Person> compare=new SerializerCompare<>(serializer,count, creator);
		compare.run();
		System.out.println(compare.toString());
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setCreator(ICreator creator) {
		this.creator = creator;
	}
	
}
