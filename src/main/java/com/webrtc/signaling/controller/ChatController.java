package com.webrtc.signaling.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.webrtc.signaling.dto.ChatDTO;

@Controller
public class ChatController {
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatDTO sendMessage(@DestinationVariable String roomId, ChatDTO message) {
    	System.out.println("메시지: " + message);
    	System.out.println("방번호: " + roomId);
        return message;
    }
}
