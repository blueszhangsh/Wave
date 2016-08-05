package com.wave.im.core;

import org.springframework.beans.factory.annotation.Autowired;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class Server {

	private ServerBootstrap bootstrap;

	private final int bossGroupThread = 1;

	private final int workerGroupThread = Runtime.getRuntime().availableProcessors() * 2;
	
	private final int port = 8898;
	
	private final LengthFieldPrepender lengthPrepender = new LengthFieldPrepender(4);
	
	@Autowired
	private HeartbeatHandler heartBeartHandler;

	public void init() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupThread);
		EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupThread);
		try {
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) 
					.childHandler(new ChannelInitializer<SocketChannel>() { 
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IdleStateHandler(300, 0, 0));
							ch.pipeline().addLast(heartBeartHandler);
							
							ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(0, 0, 4, 0, 4));
							
							ch.pipeline().addLast(lengthPrepender);
						}	
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.option(ChannelOption.SO_REUSEADDR, true)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			bootstrap.bind(port).sync(); 
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
