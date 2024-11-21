package com.webrtc.member.exception;

public class JWTValidationException extends RuntimeException {
	 private static final long serialVersionUID = 1L;
	
    public JWTValidationException(String message) {
        super(message);
    }

    public JWTValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}