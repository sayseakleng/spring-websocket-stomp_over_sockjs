package com.mcnc.socket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.mcnc.socket.core.handler.SessionHolderSubProtocolWebSocketHandler;
import com.mcnc.socket.core.interceptor.ConnectionInterceptor;
import com.mcnc.socket.core.interceptor.OnlineUserNotificationChannelInterceptorAdapter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	@Autowired 
	DelegatingWebSocketMessageBrokerConfiguration configuration;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket").setAllowedOrigins("*")
			.addInterceptors(new ConnectionInterceptor()).withSockJS();
	}
	
	@Bean
	public WebSocketHandler subProtocolWebSocketHandler() {
		AbstractSubscribableChannel clientInboundChannel = configuration.clientInboundChannel();
		
		AbstractSubscribableChannel clientOutboundChannel = configuration.clientOutboundChannel();
		clientOutboundChannel.addInterceptor(new OnlineUserNotificationChannelInterceptorAdapter());
		
		WebSocketHandler handler = new SessionHolderSubProtocolWebSocketHandler(
				clientInboundChannel, clientOutboundChannel);
		return handler;
	}
}