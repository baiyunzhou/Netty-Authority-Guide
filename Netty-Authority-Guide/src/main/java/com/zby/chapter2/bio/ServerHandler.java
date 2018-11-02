package com.zby.chapter2.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

import com.zby.chapter2.bio.processer.CommandProcesser;

/**
 * 
 * @author zhoubaiyun
 * @date 2018年6月13日
 * @Description BIO服务端处理器
 */
public class ServerHandler implements Runnable {

	private Socket socket;
	private String tip;
	private List<CommandProcesser> processers;

	public ServerHandler(Socket socket, List<CommandProcesser> processers) {
		Objects.requireNonNull(socket, "socket must not be null");
		Objects.requireNonNull(processers, "processers must not be null");
		this.socket = socket;
		this.processers = processers;
		tip = buildTip();
	}

	public void run() {
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			printWriter.println(tip);
			while (true) {
				String request = bufferedReader.readLine();
				if (null == request) {
					continue;
				}
				System.out.println("接收到客户端【" + socket.getRemoteSocketAddress() + "】请求数据：" + request);
				String response = null;
				for (CommandProcesser commandProcesser : processers) {
					if (commandProcesser.support(request)) {
						response = commandProcesser.process(socket, request);
						break;
					}
				}
				if (null == response) {
					response = tip;
				}
				System.out.println("返回给客户端【" + socket.getRemoteSocketAddress() + "】响应数据：" + response);
				printWriter.println(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != printWriter) {
				printWriter.close();
			}
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}

	private String buildTip() {
		StringBuilder tip = new StringBuilder();
		tip.append("您好，客户端！你可以执行的请求列表：");
		for (CommandProcesser commandProcesser : processers) {
			tip.append("【");
			tip.append(commandProcesser.processOption());
			tip.append("】");
		}
		return tip.toString();
	}

}
