package com.webrtc.member.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String id;
    private String password;
    private String nickname;
    private String email;
}