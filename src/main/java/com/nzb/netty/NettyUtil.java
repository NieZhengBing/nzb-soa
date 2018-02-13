package com.nzb.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyUtil {
	public static void startServer(String port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new NettyServerInHandler());
						}

					}).option(ChannelOption.SO_BACKLOG, 128);
			ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
			f.channel().closeFuture().sync();

		} catch (NumberFormatException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static String sendMsg(String host, String port, final String sendmsg)
			throws NumberFormatException, InterruptedException {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		final StringBuffer resultmsg = new StringBuffer();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					// ch.pipeline().addLast(new NettyServerInHandler(resultmsg, sendmsg));
					ch.pipeline().addLast(new NettyClientInHandller(resultmsg, sendmsg));
				}
			});
			b.connect(host, Integer.parseInt(port)).channel().closeFuture().await();
			return resultmsg.toString();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

}