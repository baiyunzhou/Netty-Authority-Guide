package com.zby.chapter2.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

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
		Socket socket = null;
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		Scanner scanner = null;
		try {
			socket = new Socket(HOST, PORT);
			SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
			System.out.println("建立起与服务端的连接，服务器地址为：	" + remoteSocketAddress);

			scanner = new Scanner(System.in);
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				System.out.println("请输入请求：");
				String request = scanner.nextLine();
				if (null == request) {
					continue;
				}
				printWriter.println(request);
				System.out.println("服务器端【" + remoteSocketAddress + "】请求数据：" + request);
				String response = bufferedReader.readLine();
				System.out.println("服务器端【" + remoteSocketAddress + "】响应数据：" + response);
			}

		} catch (Exception e) {
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
			if (null != scanner) {
				scanner.close();
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