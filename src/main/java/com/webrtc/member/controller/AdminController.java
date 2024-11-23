package com.webrtc.member.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webrtc.member.service.AdminService;
import com.webrtc.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	// 회원 탈퇴 처리
	@PutMapping("/deactivate/{memberId}")
	public ResponseEntity<Void> deactivateMember(@PathVariable String memberId) {
		log.info("회원 탈퇴 요청: {}", memberId);
		adminService.deactivateMember(memberId);
		return ResponseEntity.ok().build();
	}

	// 모든 회원 정보 조회
	@GetMapping("members")
	public ResponseEntity<List<MemberDTO>> getAllMembers() {
		List<MemberDTO> members = adminService.getAllMembers();
		System.out.println("전체 회원 목록: " + members);
		return ResponseEntity.ok(members);
	}

	// 특정 회원 정보 조회
	@GetMapping("/member/{member_id}")
	public ResponseEntity<MemberDTO> getMemberById(@PathVariable String member_id) {
		Optional<MemberDTO> member = adminService.getMemberById(member_id);
		return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
