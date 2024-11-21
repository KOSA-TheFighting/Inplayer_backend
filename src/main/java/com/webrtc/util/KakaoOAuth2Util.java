package com.webrtc.util;

import com.webrtc.member.exception.KakaoOAuth2Exception;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuth2Util {

//    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
//    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public String requestAccessToken(String code, String redirectUri) {
        try {
            // 카카오 API 요청 로직
            return "accessToken";
        } catch (Exception e) {
            throw new KakaoOAuth2Exception("Failed to request access token from Kakao", e);
        }
    }

    public JSONObject fetchUserInfo(String accessToken) {
        try {
            // 사용자 정보 요청 로직
            return new JSONObject();
        } catch (Exception e) {
            throw new KakaoOAuth2Exception("Failed to fetch user info from Kakao", e);
        }
    }
}