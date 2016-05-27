package com.mcnc.socket.core.event;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Configuration
public class StompSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
	private static final Logger logger = LoggerFactory.getLogger(StompSubscribeEventListener.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		String sessionId = (String) headers.get("simpSessionId");
		
		Map<String, String> sessionAttributes = (Map<String, String>) headers.get("simpSessionAttributes");
		String userId = sessionAttributes.get("login");
		
		String channel = (String) headers.get("simpDestination");
		logger.info("User {} with sessionId {} subscribed to channel{}", userId, sessionId, channel);
	}
}
