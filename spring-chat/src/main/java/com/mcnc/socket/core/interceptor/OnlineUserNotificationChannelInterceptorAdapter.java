package com.mcnc.socket.core.interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcnc.socket.core.handler.SessionHolderSubProtocolWebSocketHandler;
import com.mcnc.socket.message.NewMemberMessage;

public class OnlineUserNotificationChannelInterceptorAdapter extends ChannelInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(OnlineUserNotificationChannelInterceptorAdapter.class);
	public static final String USERNAME_KEY = "login";
	private static final ObjectMapper oMapper = new ObjectMapper();
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		
		MessageHeaders headers = message.getHeaders();
		SimpMessageType messageType = (SimpMessageType) headers.get("simpMessageType");
		if(SimpMessageType.CONNECT_ACK.equals(messageType)) {
			
			List<NewMemberMessage> users = new ArrayList<>();
			Collection<WebSocketSession> sessions = SessionHolderSubProtocolWebSocketHandler.getSessions();
			for (WebSocketSession webSocketSession : sessions) {
				String userId = (String) webSocketSession.getAttributes().get(USERNAME_KEY);
				
				// prevent attribute login isn't set yet
				if(userId == null && webSocketSession.getId().equals(headers.get("simpSessionId"))) {
					Message<?> requestMessage = (Message<?>) headers.get("simpConnectMessage");
					MessageHeaders requestHeaders = requestMessage.getHeaders();
					@SuppressWarnings("unchecked")
					Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) requestHeaders.get("nativeHeaders");
					try {
						List<String> list = nativeHeaders.get("login");
						userId = list.get(0);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
				
				NewMemberMessage user = new NewMemberMessage(userId);
				users.add(user);
			}
			
			
			try {
				message = MessageBuilder.createMessage(oMapper.writeValueAsBytes(users), headers);
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return super.preSend(message, channel);
	}
}