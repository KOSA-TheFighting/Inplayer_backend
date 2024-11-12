package com.webrtc.signaling.model.vo;

import lombok.Data;

@Data
public class ChatRoomVO {
	private int    chatroom_id;
	private int    stream_id;
	private String chatroom_status;
}
