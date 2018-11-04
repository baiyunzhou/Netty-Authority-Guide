package com.zby.chapter4_3;

import io.netty.bootstrap.Bootstrap;
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
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 
 * @author zby
 * @date 2018年11月3日
 * @description 考虑拆包粘包的timer客户端，LineBasedFrameDecoder+StringDecoder
 */
public class TimerClient {

	public static void main(String[] args) throws Exception {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			ChannelFuture channelFuture = bootstrap.group(worker).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new LineBasedFrameDecoder(1024));
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new TimerClientHandler());
						}
					}).connect("localhost", 8080).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
		}
	}

	private static class TimerClientHandler extends ChannelInboundHandlerAdapter {
		private int counter;
		private byte[] data;

		public TimerClientHandler() {
			data = ("query time order" + System.lineSeparator()).getBytes();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ByteBuf request = null;
			for (int i = 0; i < 100; i++) {
				request = Unpooled.buffer(data.length);
				request.writeBytes(data);
				ctx.writeAndFlush(request);

			}
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			String resonse = (String) msg;
			System.out.println("now is :" + resonse + ";counter:" + ++counter);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}
}