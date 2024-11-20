package com.webrtc.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoInfo {
    private String id;         // 카카오 사용자 ID
    private String nickname; // 카카오 사용자 닉네임
    private String email;    // 카카오 사용자 이메일
}