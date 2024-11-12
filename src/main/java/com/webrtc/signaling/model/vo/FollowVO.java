package com.webrtc.signaling.model.vo;

import lombok.Data;

@Data
public class FollowVO {
	private int    follow_id;
	private String member_id;
	private String created_time;
	private String followed_member_id;
}
