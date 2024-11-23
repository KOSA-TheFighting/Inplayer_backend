package com.webrtc.member.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@PatchMapping("/updateNickname")
	public ResponseEntity<String> updateNickname(
			@RequestParam String member_id,
			@RequestParam String newNickname) {
		memberService.updateNickname(member_id, newNickname);
		return ResponseEntity.ok("닉네임 수정 완료!");
	}


}