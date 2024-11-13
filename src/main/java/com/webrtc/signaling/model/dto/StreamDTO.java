package com.webrtc.signaling.model.dto;

import lombok.Data;

@Data
public class StreamDTO {
	private String member_id;
	private String member_nickname;
	
	private String streamtag_num;
	private String stream_title;
	private String stream_description;
	private String stream_start_time;
	private String stream_status;
	
	private int    stream_view_count;   //방송 조회수
	private String chatroom_status;     //채팅방 상태
	private int    stream_viewer_count; //방송 실시간 시청자수
}
