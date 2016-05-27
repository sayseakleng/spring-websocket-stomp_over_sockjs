package com.mcnc.socket.core.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

public class SessionHolderSubProtocolWebSocketHandler extends SubProtocolWebSocketHandler {
	private static final Logger logger = LoggerFactory.getLogger(SessionHolderSubProtocolWebSocketHandler.class);
	private static final Map<String, WebSocketSession> sessions = new HashMap<>();

	public SessionHolderSubProtocolWebSocketHandler(MessageChannel clientInboundChannel,
			SubscribableChannel clientOutboundChannel) {
		super(clientInboundChannel, clientOutboundChannel);
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		attachSession(sessionId, session);
		logger.info("Session connected: {}", sessionId);
		super.afterConnectionEstablished(session);
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		String sessionId = session.getId();
		detachSession(sessionId);
		logger.info("Session disconnected: {}", sessionId);
		
		super.afterConnectionClosed(session, closeStatus);
	}
	
	
	public static void attachSession(String sessionId, WebSocketSession session) {
		sessions.put(sessionId, session);
	}
	

	public static WebSocketSession detachSession(String sessionId) {
		return sessions.remove(sessionId);
	}
	
	public static WebSocketSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	public static Collection<WebSocketSession> getSessions() {
		return sessions.values();
	}

}
