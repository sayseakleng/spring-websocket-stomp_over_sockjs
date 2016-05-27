package com.mcnc.socket.service.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.mcnc.socket.channel.ChannelFactory;
import com.mcnc.socket.channel.ChannelMessageType;
import com.mcnc.socket.message.NewMemberMessage;
import com.mcnc.socket.message.NewMessage;
import com.mcnc.socket.message.OfflineMessage;
import com.mcnc.socket.service.ChannelMessageService;

@Service
public class ChannelMessageServiceImpl implements ChannelMessageService {
	@Autowired
	private SimpMessagingTemplate messageSender;
	
	@Autowired
	private ApplicationContext appContext;

	@Override
	public void notifyNewUser(String destination, NewMemberMessage msg) {
		messageSender.convertAndSend(destination, msg);
	}

	@Override
	public void notifyOffine(String destination, OfflineMessage msg) {
		messageSender.convertAndSend(destination, msg);
	}

	@Override
	public void notifyNewMessage(String destination, NewMessage msg) {
		messageSender.convertAndSend(destination, msg);
		
		printReceivers();
	}
	
	
	private void printReceivers() {
		SimpleBrokerMessageHandler halder = (SimpleBrokerMessageHandler) appContext.getBean("simpleBrokerMessageHandler");
		SubscriptionRegistry subscriptionRegistry = halder.getSubscriptionRegistry();
		
		SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		accessor.setDestination(ChannelFactory.build(ChannelMessageType.NEW_MESSAGE));
		Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
		
		MultiValueMap<String, String> subscriptions = subscriptionRegistry.findSubscriptions(message);
		Set<Entry<String,List<String>>> entrySet = subscriptions.entrySet();
		
		int i = 1;
		System.out.println("Sent to:");
		for (Entry<String, List<String>> entry : entrySet) {
			System.out.printf("%d - [sessionId: %s, subscriptionId: %s]\n", i++, entry.getKey(), entry.getValue());
		}
	}

}
