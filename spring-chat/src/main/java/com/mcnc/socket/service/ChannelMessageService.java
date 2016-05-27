package com.mcnc.socket.service;

import com.mcnc.socket.message.NewMemberMessage;
import com.mcnc.socket.message.NewMessage;
import com.mcnc.socket.message.OfflineMessage;

public interface ChannelMessageService {
	void notifyNewUser(String destination, NewMemberMessage msg);
	void notifyOffine(String destination, OfflineMessage msg);
	void notifyNewMessage(String destination, NewMessage msg);
}
