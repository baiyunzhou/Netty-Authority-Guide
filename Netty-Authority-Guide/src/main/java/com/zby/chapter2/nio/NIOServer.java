package com.zby.chapter2.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * 
 * @author zby
 * @date 2018年11月3日
 * @description NIO服务端
 */
public class NIOServer {
	public static final int DEFAULT_PORT = 8080;
	public static final int DEFAULT_BACKLOG = 1024;
	public static final boolean DEFAULT_BLOCK = false;
	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;

	public NIOServer(int port, int backlog, boolean block) throws IOException {
		SelectorProvider provider = SelectorProvider.provider();
		serverSocketChannel = provider.openServerSocketChannel();
		selector = provider.openSelector();
		serverSocketChannel.bind(new InetSocketAddress(port), backlog);
		serverSocketChannel.configureBlocking(block);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("The server listen on :" + port);
	}

	/**
	 * 
	 * @Description 打印支持的选项。SO_RCVBUF、SO_REUSEADDR、IP_TOS。获取IP_TOS选项会报错
	 */
	public void printOption() throws Exception {
		Set<SocketOption<?>> supportedOptions = serverSocketChannel.supportedOptions();
		System.out.println("supportedOptions");
		for (SocketOption<?> socketOption : supportedOptions) {
			// Object option = serverSocketChannel.getOption(socketOption);
			System.out.println(socketOption);
		}
	}

	public void start() throws Exception {
		for (;;) {
			selector.select(1000);
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			for (SelectionKey selectionKey : selectedKeys) {
				if (selectionKey.isValid()) {
					if (selectionKey.isAcceptable()) {
						handleAccept(selectionKey);
					} else if (selectionKey.isReadable()) {
						handleRead(selectionKey);
					} else {

					}
				}
			}
		}

	}

	/**
	 * 
	 * @author zhoubaiyun
	 * @date 2018年6月14日
	 * @param selectionKey
	 * @throws IOException
	 * @Description 处理新建连接
	 */
	private void handleAccept(SelectionKey selectionKey) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	private void handleRead(SelectionKey selectionKey) throws IOException {
		SocketChannel SocketChannel = (SocketChannel) selectionKey.channel();
		ByteBuffer dst = ByteBuffer.allocate(1024);
		int read = SocketChannel.read(dst);
		if (read > 0) {
			dst.flip();
			byte[] bytes = new byte[dst.remaining()];
			dst.get(bytes);
			String request = new String(bytes, 0, read);
			System.out.println(request);
		} else if (read < 0) {
			selectionKey.cancel();
			SocketChannel.close();
		}
		SocketChannel.write(dst);
	}

	public static void main(String[] args) throws Exception {
		if (0 == args.length) {
			new NIOServer(DEFAULT_PORT, DEFAULT_BACKLOG, DEFAULT_BLOCK).start();
		} else if (1 == args.length) {
			new NIOServer(Integer.parseInt(args[0]), DEFAULT_BACKLOG, DEFAULT_BLOCK).start();
		} else if (2 == args.length) {
			new NIOServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), DEFAULT_BLOCK).start();
		} else {
			new NIOServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Boolean.parseBoolean(args[2])).start();
		}
	}
}
