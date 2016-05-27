package com.mcnc.socket.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.MultiValueMap;

import com.mcnc.socket.channel.ChannelFactory;
import com.mcnc.socket.channel.ChannelMessageType;
import com.mcnc.socket.main.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class TestChannelMessageService {
	@Autowired
	ApplicationContext context;
	
	@Test
	public void test() {
		assertNotNull(context);
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		for (String string : beanDefinitionNames) {
			System.out.println(string);
		}
		
		SimpleBrokerMessageHandler halder = (SimpleBrokerMessageHandler) context.getBean("simpleBrokerMessageHandler");
		SubscriptionRegistry subscriptionRegistry = halder.getSubscriptionRegistry();
		
		SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		accessor.setDestination(ChannelFactory.build(ChannelMessageType.NEW_MESSAGE));
		Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
		
		MultiValueMap<String, String> subscriptions = subscriptionRegistry.findSubscriptions(message);
		Set<Entry<String,List<String>>> entrySet = subscriptions.entrySet();
		for (Entry<String, List<String>> entry : entrySet) {
			System.out.printf("[sessionId: %s, subscriptionId: %s]\n", entry.getKey(), entry.getValue());
		}
	}

}
