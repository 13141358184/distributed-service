package com.xiaoheiwu.service.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.xiaoheiwu.service.serializer.exception.NotSupportSerializerException;
import com.xiaoheiwu.service.serializer.stream.DataInput;
import com.xiaoheiwu.service.serializer.stream.DataOutput;
import com.xiaoheiwu.service.serializer.stream.impl.ByteDataInput;

public abstract class AbstractSerializer implements ISerializer{



	@Override
	public void writeObject(Object object, OutputStream out)
			throws NotSupportSerializerException, IOException {
		DataOutputStream dataOut=new DataOutputStream(out);
		DataOutput output = serialize(object);
		byte[] objectBytes=output.getData();
		dataOut.writeInt(objectBytes.length);
		dataOut.write(objectBytes);
		dataOut.flush();
	}

	@Override
	public Object readObject(InputStream in) throws NotSupportSerializerException,
			IOException {
		DataInputStream dataInput=new DataInputStream(in);
		int size=dataInput.readInt();
		byte[] objectBytes=new byte[size];
		dataInput.read(objectBytes);
		DataInput input=new ByteDataInput(objectBytes);
		Object object=deserialize(input);
		return object;
	}

}
