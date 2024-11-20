package com.webrtc.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@Builder
public class MemberVO {
	private String member_id;
	private String member_name;
	private String member_nickname;
	private String member_email;
	private String member_created_date;
	private int member_delete_yn;
	private String member_last_login;
	private String member_last_logout;
}
