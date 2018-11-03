package com.zby.chapter2.bio;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

import com.zby.chapter2.bio.thread.ReadInputStream;
import com.zby.chapter2.bio.thread.WriteOutputStream;

/**
 * 
 * @author zby
 * @date 2018年11月3日
 * @description BIO客户端
 */
public class BIOClient {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 8080;

	public static void main(String[] args) {
		if (0 == args.length) {
			start(HOST, PORT);
		} else if (1 == args.length) {
			start(args[0], PORT);
		} else {
			start(args[0], Integer.parseInt(args[1]));
		}
	}

	public static void start(String host, int port) {
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