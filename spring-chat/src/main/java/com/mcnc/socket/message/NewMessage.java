package com.mcnc.socket.message;

public class NewMessage {
	private String message;
	private String sender;
	
	public NewMessage() {
		
	}
	
	public NewMessage(String message, String sender) {
		this.message = message;
		this.sender = sender;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
}
