package com.mcnc.socket.message;

public class OfflineMessage {
	private String userId;
	
	public OfflineMessage() {
		
	}
	
	public OfflineMessage(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
