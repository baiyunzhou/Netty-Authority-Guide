package com.zby.chapter1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author zhoubaiyun
 * @date 2018年6月13日
 * @Description BIO服务端
 */
public class BIOServer {
	private static final int PORT = 1314;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("The server listen on port:" + PORT);
			Socket socket = null;
			while (true) {
				socket = serverSocket.accept();
				System.out.println("新建客户端连接,客户端地址为：		" + socket.getRemoteSocketAddress());
				new Thread(new ServerHandler(socket)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != serverSocket) {
				System.out.println("The server close...");
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				serverSocket = null;
			}
		}

	}

}
