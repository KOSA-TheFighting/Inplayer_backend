package com.webrtc.member.exception;

//멤버가 존재할 경우 예외 객체
public class ExistMemberException extends Exception {
	 private static final long serialVersionUID = 1L;
	
	public ExistMemberException(String email) {
		super(email + "계정이 존재합니다");
	}
}
