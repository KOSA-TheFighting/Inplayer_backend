package com.webrtc.member.exception;

public class KakaoOAuth2Exception extends RuntimeException {
    public KakaoOAuth2Exception(String message) {
        super(message);
    }

    public KakaoOAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
