package com.webrtc.member.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webrtc.member.model.dto.MemberDTO;
import com.webrtc.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private final MemberService memberService;
	
//	@GetMapping("/auth/kakao/login")
//	public ResponseEntity<String> kakaoLogin() throws IOException {
//		String kakaoAuthUrl = String.format(kakaoHttp, kakaoAppKey, // 카카오 앱 키
//				URLEncoder.encode(kakaoCallback, "UTF-8"));
//		return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).header("Access-Control-Expose-Headers", "Location")
//				.header("Location", kakaoAuthUrl).build();
//	}

	@GetMapping("getInfo/{member_id}")
	public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable String member_id) {
	    Optional<MemberDTO> response = memberService.getInfo(member_id);
	    
	    if (response.isPresent()) {
	    	System.out.println("멤버 정보 응답:" + response.get());
	        return ResponseEntity.ok(response.get());
	    } else {
	    	System.out.println("멤버 정보 응답: 실패");
	        return ResponseEntity.notFound().build();
	    }
	}
}
