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
	private String member_id;              // API로 받은 고유 ID
	private String member_name;            // API로 받은 nickname
	private String member_nickname;        // 회원 가입 시 사용자로부터 받을 닉네임
	private String member_email;           // API로 받은 이메일
	private LocalDateTime member_created_date; // 가입 날짜 (yyyy.MM.dd hh:mm:ss)
	private int member_delete_yn;           // 탈퇴 여부 플래그 (0: 활성화, 1: 탈퇴)
	private LocalDateTime member_last_login;    // 마지막 로그인 일시
	private LocalDateTime member_last_logout;   // 마지막 로그아웃 일시
}



