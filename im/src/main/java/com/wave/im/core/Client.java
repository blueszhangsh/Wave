package com.wave.im.core;

import io.netty.channel.ChannelHandlerContext;

public class Client {

	private long userId;
	
	private ChannelHandlerContext socket;

	
	public Client(long userId, ChannelHandlerContext ctx) {
		this.userId = userId;
		this.socket = ctx;
	}
	
	public long getUserId() {
		return userId;
	}

	public ChannelHandlerContext getSocket() {
		return socket;
	}

}
