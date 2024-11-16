package com.webrtc.member.model.dto;

import lombok.Data;

@Data
public class MemberDTO {
	private String member_id;
	private String member_name;
	private String member_nickname;
	private String member_email;
	private String member_created_date;
	private int    member_delete_yn;
	private String member_last_login;
	private String member_last_logout;
	
	private int    followerNum;
}
