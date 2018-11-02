package com.zby.chapter2.bio;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

import com.zby.chapter2.bio.thread.ReadInputStream;
import com.zby.chapter2.bio.thread.WriteOutputStream;

/**
 * 
 * @author zhoubaiyun
 * @date 2018年6月13日
 * @Description NIO客户端
 */
public class BIOClient {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 1314;

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		Socket socket = null;
		try {
			socket = new Socket(HOST, PORT);
			SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
			System.out.println("建立起与服务端的连接，服务器地址为：" + remoteSocketAddress);
			new Thread(new ReadInputStream(socket.getInputStream()), "read-thread").start();
			new Thread(new WriteOutputStream(socket.getOutputStream()), "write-thread").start();
			for (;;) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
		}
	}
}