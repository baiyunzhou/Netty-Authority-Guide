package com.zby.chapter3;

import java.nio.charset.Charset;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	public static void main(String[] args) throws Exception {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			ChannelFuture channelFuture = bootstrap.group(worker).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true).handler(new ChildChannelHandler())
					.connect("localhost", 8080).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
		}
	}

	private static class ChildChannelHandler extends ChannelInboundHandlerAdapter {
		private final ByteBuf firstMsg;

		public ChildChannelHandler() {
			byte[] data = "query time".getBytes();
			firstMsg = Unpooled.buffer(data.length);
			firstMsg.writeBytes(data);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush(firstMsg);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			Charset utf8 = Charset.forName("UTF-8");
			ByteBuf byteBuf = (ByteBuf) msg;
			byte[] data = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(data);
			String request = new String(data, utf8);
			System.out.println("now is :" + request);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}
}
