package com.mcnc.socket.core.event;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.mcnc.socket.channel.ChannelFactory;
import com.mcnc.socket.channel.ChannelMessageType;
import com.mcnc.socket.message.OfflineMessage;
import com.mcnc.socket.service.ChannelMessageService;

@Configuration
public class WebSocketUnconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketUnconnectEventListener.class);
	@Autowired
	private ChannelMessageService service;
	
	@Override
	@SuppressWarnings("unchecked")
	public void onApplicationEvent(SessionDisconnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		String sessionId = (String) headers.get("simpSessionId");
		
		Map<String, String> sessionAttributes = (Map<String, String>) headers.get("simpSessionAttributes");
		String userId = sessionAttributes.get("login");
		
		logger.info("User {} with sessionId {} unconnected", userId, sessionId);
		
		
		// notify old session users 
		OfflineMessage msg = new OfflineMessage(userId);
		service.notifyOffine(ChannelFactory.build(ChannelMessageType.OFFLINE), msg);
	}
}
