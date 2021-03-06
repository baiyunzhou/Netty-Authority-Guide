package com.zby.chapter4;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.ServerBootstrapConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;

/**
 * 
 * @author zby
 * @date 2018年11月3日
 * @description 不考虑拆包粘包的timer服务器
 */
public class TimerServer {

	public static void main(String[] args) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			ServerBootstrapConfig serverBootstrapConfig = serverBootstrap.config();
			System.out.println("serverBootstrapConfig:" + serverBootstrapConfig);
			ChannelFuture channelFuture = serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
					.localAddress("127.0.0.1", 8080).option(ChannelOption.SO_BACKLOG, 1024).attr(AttributeKey.valueOf("name"), "zby")
					.childOption(ChannelOption.SO_KEEPALIVE, true).childAttr(AttributeKey.valueOf("name"), "child")
					.handler(new LoggingHandler(TimerServer.class, LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new TimerServerHandler());
						}
					}).bind().sync();
			System.out.println("serverBootstrapConfig:" + serverBootstrapConfig);
			channelFuture.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	private static class TimerServerHandler extends ChannelInboundHandlerAdapter {
		private int counter;

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			Charset utf8 = Charset.forName("UTF-8");
			ByteBuf byteBuf = (ByteBuf) msg;
			byte[] data = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(data);
			String request = new String(data, utf8).substring(0, data.length - System.lineSeparator().length());
			System.out.println("request:" + request + ";counter:" + ++counter);
			@SuppressWarnings("deprecation")
			String response = "query time order".equalsIgnoreCase(request) ? new Date().toLocaleString() + System.lineSeparator()
					: "Bad Request" + System.lineSeparator();
			ByteBuf buffer = Unpooled.copiedBuffer(response.getBytes(utf8));
			ctx.writeAndFlush(buffer);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}

}