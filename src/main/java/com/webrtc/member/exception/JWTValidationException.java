package com.webrtc.member.exception;

public class JWTValidationException extends RuntimeException {
    public JWTValidationException(String message) {
        super(message);
    }

    public JWTValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}