package com.mcnc.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mcnc.socket.channel.ChannelFactory;
import com.mcnc.socket.channel.ChannelMessageType;
import com.mcnc.socket.message.NewMessage;
import com.mcnc.socket.service.ChannelMessageService;

@Controller
public class ChatController {
	
	@Autowired
	private ChannelMessageService service;
	
	@RequestMapping("/")
	public String getHomePage() {
		return "redirect:index.html";
	}
	

    @MessageMapping("/chat")  
    public void sentChat(NewMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
    	String userId = (String) headerAccessor.getSessionAttributes().get("login");
    	message.setSender(userId);
    	
    	service.notifyNewMessage(ChannelFactory.build(ChannelMessageType.NEW_MESSAGE), message);
    }

}
