package com.webrtc.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVO {
	private String member_id;
	private String member_name;
	private String member_nickname;
	private String member_email;
	private String member_created_date;
	private int    member_delete_yn;
	private String member_last_login;
	private String member_last_logout;
}
