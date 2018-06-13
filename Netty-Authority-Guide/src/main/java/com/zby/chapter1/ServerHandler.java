package com.zby.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

/**
 * 
 * @author zhoubaiyun
 * @date 2018年6月13日
 * @Description BIO服务端处理器
 */
public class ServerHandler implements Runnable {
	private static final String QUERY_TIME = "query time";
	private Socket socket;
	private SocketAddress remoteSocketAddress;

	public ServerHandler(Socket socket) {
		this.socket = socket;
		this.remoteSocketAddress = socket.getRemoteSocketAddress();
	}

	public void run() {
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			while (true) {
				String request = bufferedReader.readLine();
				if (null == request) {
					continue;
				}
				System.out.println("接收到客户端【" + remoteSocketAddress + "】请求数据：" + request);
				String response = null;
				if (QUERY_TIME.equalsIgnoreCase(request)) {
					response = "Now is : " + new Date().toString();
				} else {
					response = "Only process :" + QUERY_TIME;
				}
				System.out.println("返回给客户端【" + remoteSocketAddress + "】响应数据：" + response);
				printWriter.println(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (null != printWriter) {
				printWriter.close();
			}
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
