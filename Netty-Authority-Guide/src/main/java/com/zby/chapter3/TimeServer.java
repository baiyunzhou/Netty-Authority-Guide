package com.zby.chapter3;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	public static void main(String[] args) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			ChannelFuture channelFuture = serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler()).bind(8080).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	private static class ChildChannelHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			Charset utf8 = Charset.forName("UTF-8");
			ByteBuf byteBuf = (ByteBuf) msg;
			byte[] data = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(data);
			String request = new String(data, utf8);
			System.out.println("request:" + request);
			@SuppressWarnings("deprecation")
			String response = "query time".equalsIgnoreCase(request) ? new Date().toLocaleString() : "Bad Request";
			ByteBuf buffer = Unpooled.copiedBuffer(response.getBytes(utf8));
			ctx.write(buffer);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}

}
