package com.mcnc.socket.core.event;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.mcnc.socket.channel.ChannelFactory;
import com.mcnc.socket.channel.ChannelMessageType;
import com.mcnc.socket.core.handler.SessionHolderSubProtocolWebSocketHandler;
import com.mcnc.socket.message.NewMemberMessage;
import com.mcnc.socket.service.ChannelMessageService;

@Configuration
public class WebSocketConnectEventListener implements ApplicationListener<SessionConnectEvent> {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConnectEventListener.class);
	
	@Autowired
	private ChannelMessageService service;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		String sessionId = (String) headers.get("simpSessionId");
		
		Map<String, String> sessionAttributes = (Map<String, String>) headers.get("simpSessionAttributes");
		Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) headers.get("nativeHeaders");
		String userId = null;
		try {
			List<String> list = nativeHeaders.get("login");
			userId = list.get(0);
		} catch (Exception e) {
		}
		
		
		if(userId == null || "".equals(userId)) {
			WebSocketSession detachSession = SessionHolderSubProtocolWebSocketHandler.detachSession(sessionId);
			try {
				detachSession.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			logger.error("Connection rejected (sessionId: {}, user: {})", sessionId, userId);
		}
		else {
			sessionAttributes.put("login", userId);
			logger.info("Connection allowed (sessionId: {}, user: {})", sessionId, userId);
			
			
			// notify new session to old session users 
			NewMemberMessage msg = new NewMemberMessage(userId);
			service.notifyNewUser(ChannelFactory.build(ChannelMessageType.NEW_USER), msg);
		}
		
	}
}
