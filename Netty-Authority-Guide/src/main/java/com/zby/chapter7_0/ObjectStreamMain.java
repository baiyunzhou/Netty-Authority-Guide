package com.zby.chapter7_0;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectStreamMain {

	public static void main(String[] args) throws Exception {
		UserInfo obj = new UserInfo(1, "obj");
		System.out.println(obj);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(obj);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		objectOutputStream.close();
		ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
		UserInfo serializebleObj = (UserInfo) objectInputStream.readObject();
		System.out.println(serializebleObj);
		objectInputStream.close();
	}

}
