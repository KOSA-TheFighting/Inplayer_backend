package com.webrtc.signaling.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.webrtc.signaling.model.dto.ChatMessageDTO;
import com.webrtc.signaling.service.ChatMessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/chat/{roomId}")
	public ChatMessageDTO sendMessage(@DestinationVariable String roomId, ChatMessageDTO message) {
		System.out.println("메시지: " + message);
		System.out.println("방번호: " + roomId);

		try {
			chatMessageService.saveMessage(roomId, message);
			return message;
		} catch (Exception e) {
			log.error("Error processing chat message", e);
			throw new RuntimeException("메시지 처리 중 오류가 발생했습니다.", e);
		}
	}
}
