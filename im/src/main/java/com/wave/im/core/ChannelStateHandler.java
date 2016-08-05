package com.wave.im.core;

import org.springframework.beans.factory.annotation.Autowired;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class ChannelStateHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	ConnectedClients connectedClients;
	
	AttributeKey<Long> userIdKey = AttributeKey.valueOf("userId");
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		final Long userId = (Long) ctx.channel().attr(userIdKey).get();
		if (userId == null) {
			return;
		}
		connectedClients.removeClient(new Client(userId, ctx));
	}
	
}
