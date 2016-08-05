package com.wave.im.core;

import org.springframework.beans.factory.annotation.Autowired;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

@Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	ConnectedClients connectedClients;
	
	AttributeKey<Long> userIdKey = AttributeKey.valueOf("userId");
	
	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			if (((IdleStateEvent) evt).state().equals(IdleStateEvent.READER_IDLE_STATE_EVENT)) {
				final Long userId = (Long) ctx.channel().attr(userIdKey).get();
				if (userId == null) {
					super.userEventTriggered(ctx, evt);
					return;
				}
				connectedClients.removeClient(new Client(userId, ctx));
				ctx.channel().close();
				return;
			}
		}
		super.userEventTriggered(ctx, evt);
	}

}
