package com.webrtc.signaling.model.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChatMessageVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int    chatmessage_id;
	private String member_id;
	private int    chatroom_id;
	private String messagetype_num;
	private String chatmessage_content;
	private String chatmessage_sent_time;
	
	@Builder
	public ChatMessageVO(String member_id, int chatroom_id, String messagetype_num, String chatmessage_content, String chatmessage_sent_time) {
		this.member_id = member_id;
		this.chatroom_id = chatroom_id;
		this.messagetype_num = messagetype_num;
		this.chatmessage_content = chatmessage_content;
		this.chatmessage_sent_time = chatmessage_sent_time;
	}
}
