package com.zby.chapter2.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NIOClient {
	private static final String REMOTE_HOST = "localhost";
	private static final int REMOTE_PORT = 3344;
	private Selector selector;
	private SocketChannel socketChannel;

	public NIOClient() throws Exception {
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}

	public static void main(String[] args) throws Exception {
		NIOClient nioClient = new NIOClient();
		nioClient.doconnect();
		nioClient.doIO();
	}

	private void doIO() throws Exception {
		selector.select(1000);
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		for (SelectionKey selectionKey : selectedKeys) {
			if (selectionKey.isValid()) {

			}
		}
	}

	private void doconnect() throws Exception {
		if (socketChannel.connect(new InetSocketAddress(REMOTE_HOST, REMOTE_PORT))) {
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		} else {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}

	private void doWrite(SocketChannel socketChannel) {
		ByteBuffer request = ByteBuffer.allocate(1024);
		// socketChannel.write(request);

	}
}
