package com.zby.chapter2.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * 
 * @author zhoubaiyun
 * @date 2018年6月13日
 * @Description NIO服务端
 */
public class NIOServer {
	private static final int PORT = 3344;
	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;

	public NIOServer() throws IOException {
		SelectorProvider provider = SelectorProvider.provider();
		serverSocketChannel = provider.openServerSocketChannel();
		serverSocketChannel.bind(new InetSocketAddress(PORT));
		serverSocketChannel.configureBlocking(false);
		selector = provider.openSelector();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("The server listen on :" + PORT);
	}

	public static void main(String[] args) throws Exception {
		new NIOServer().start();
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
}
