package com.mcnc.socket.channel;

public enum ChannelMessageType {
	NEW_USER("/user"),
	NEW_MESSAGE("/message"),
	OFFLINE("/offline");
	
	private String channelSuffix;
	private ChannelMessageType(String channelSuffix) {
		this.channelSuffix = channelSuffix;
	}
	public String getChannelSuffix() {
		return channelSuffix;
	}
	
}
