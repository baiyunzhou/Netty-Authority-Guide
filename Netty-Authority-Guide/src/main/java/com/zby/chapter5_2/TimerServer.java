package com.zby.chapter5_2;

import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TimerServer {

	public static void main(String[] args) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			ChannelFuture channelFuture = serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new FixedLengthFrameDecoder(20));
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new TimerServerHander());
						}
					}).bind(8080).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	private static class TimerServerHander extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("request:" + msg);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}

}