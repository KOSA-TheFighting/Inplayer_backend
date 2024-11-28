package com.webrtc.signaling.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatMessageDTO {
	
	private String type; // 메시지 타입
    private int    roomId; // 방 번호
    private String sender; // 채팅 보낸 사람 member_id
    private String nickname; //채팅 보낸 사람 닉네임
    private String message; // 메시지
    private String time; // 채팅 발송 시간
}
