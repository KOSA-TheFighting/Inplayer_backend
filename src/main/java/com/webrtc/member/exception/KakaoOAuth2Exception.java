package com.webrtc.member.exception;

public class KakaoOAuth2Exception extends RuntimeException {
	 private static final long serialVersionUID = 1L;
	
    public KakaoOAuth2Exception(String message) {
        super(message);
    }

    public KakaoOAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
