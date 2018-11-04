package com.zby.chapter7_0;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class SizeMain {

	public static void main(String[] args) throws Exception {

		UserInfo userInfo = new UserInfo(1, "name");
		byte[] codec = codec(userInfo);
		System.out.println("bytebuffer size:" + codec.length);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(userInfo);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		System.out.println("serializable size:" + byteArray.length);

	}

	public static byte[] codec(UserInfo userInfo) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.putInt(userInfo.getId());
		byte[] nameBytes = userInfo.getName().getBytes();
		byteBuffer.putInt(nameBytes.length);
		byteBuffer.put(nameBytes);
		byteBuffer.flip();
		byte[] objData = new byte[byteBuffer.remaining()];
		byteBuffer.get(objData);
		return objData;
	}
}
