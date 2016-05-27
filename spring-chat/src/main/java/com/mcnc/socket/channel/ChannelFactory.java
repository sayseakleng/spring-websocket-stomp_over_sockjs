package com.mcnc.socket.channel;

public class ChannelFactory {
	public static String build(ChannelMessageType type) {
		return "/topic/chat".concat(type.getChannelSuffix());
	}
}
