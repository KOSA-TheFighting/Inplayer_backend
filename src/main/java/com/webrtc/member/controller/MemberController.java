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
public class MemberController {	private final MemberService memberService;

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