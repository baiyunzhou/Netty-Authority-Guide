package com.zby.chapter2.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.zby.chapter2.bio.info.ClientInfo;
import com.zby.chapter2.bio.processer.CommandProcesser;
import com.zby.chapter2.bio.processer.impl.ClientInfoCommandProcesser;
import com.zby.chapter2.bio.processer.impl.JavaScriptCommandProcesser;
import com.zby.chapter2.bio.processer.impl.TimeCommandProcesser;

/**
 * 
 * @author zby
 * @date 2018年11月3日
 * @description BIO服务端
 */
public class BIOServer {
	private static final int PORT = 8080;
	private static final int BACKLOG = 1024;
	private static final ConcurrentMap<Socket, ClientInfo> CONCURRENT_MAP = new ConcurrentHashMap<>();
	private static final AtomicInteger idGenerator = new AtomicInteger(1);

	public static void main(String[] args) {
		if (0 == args.length) {
			start(PORT, BACKLOG);
		} else if (1 == args.length) {
			start(Integer.parseInt(args[0]), BACKLOG);
		} else {
			start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
	}

	public static void start(int port, int backlog) {
		ServerSocket serverSocket = null;
		ExecutorService executorService = Executors.newFixedThreadPool(20, new ThreadFactory() {
			private AtomicInteger id = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable runnable) {
				return new Thread(runnable, "server-handler-thread" + id.getAndIncrement());
			}
		});
		try {
			serverSocket = new ServerSocket(port, backlog);
			System.out.println("服务器启动完成:" + serverSocket.getLocalSocketAddress());
			Socket socket = null;
			List<CommandProcesser> commandProcessers = new ArrayList<>();
			commandProcessers.add(new TimeCommandProcesser());
			commandProcessers.add(new ClientInfoCommandProcesser(CONCURRENT_MAP));
			commandProcessers.add(new JavaScriptCommandProcesser());
			while (true) {
				socket = serverSocket.accept();
				int id = idGenerator.getAndIncrement();
				System.out.println("新建客户端连接,客户端地址为：	" + socket.getRemoteSocketAddress());
				System.out.println("当前连接数：" + id);
				CONCURRENT_MAP.put(socket,
						new ClientInfo(id, "client" + id, socket.getRemoteSocketAddress().toString()));
				executorService.execute(new ServerHandler(socket, commandProcessers));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != serverSocket) {
				System.out.println("正在关闭服务器...");
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.err.println("服务器关闭失败...");
					e.printStackTrace();
				}
				serverSocket = null;
			}
		}
	}
}
