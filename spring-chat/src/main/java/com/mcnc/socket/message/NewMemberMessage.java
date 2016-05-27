package com.mcnc.socket.message;

public class NewMemberMessage {
	private String userId;
	
	public NewMemberMessage() {
		
	}
	
	public NewMemberMessage(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
