//package com.webrtc.member.controller;
//
//import java.io.IOException;
//
//import org.apache.catalina.util.URLEncoder;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController("/api/member")
//public class MemberController {
//	   @GetMapping("/auth/kakao/login")
//	   public ResponseEntity<String> kakaoLogin() throws IOException {
//	      String kakaoAuthUrl = String.format(kakaoHttp, kakaoAppKey, // 카카오 앱 키
//	            URLEncoder.encode(kakaoCallback, "UTF-8"));
//	      return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).header("Access-Control-Expose-Headers", "Location")
//	            .header("Location", kakaoAuthUrl).build();
//	   }
//}
