package com.webrtc.member.controller;

import java.util.List;
import java.util.Map;
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
	@PutMapping("/toggle-status/{member_id}")
	public ResponseEntity<Void> toggleMemberStatus(
			@PathVariable String member_id,
			@RequestBody Map<String, Integer> request) {
		Integer newStatus = request.get("newStatus");
		if (member_id == null || newStatus == null) {
			log.error("회원 ID 또는 상태 값이 전달되지 않았습니다.");
			return ResponseEntity.badRequest().build();
		}

		log.info("회원 상태 변경 요청: {}, 상태: {}", member_id, newStatus);
		adminService.updateMemberStatus(member_id, newStatus);
		return ResponseEntity.ok().build();
	}

	// 모든 회원 정보 조회
//	@GetMapping("members")
//	public ResponseEntity<List<MemberDTO>> getAllMembers() {
//		List<MemberDTO> members = adminService.getAllMembers();
//		System.out.println("전체 회원 목록: " + members);
//		return ResponseEntity.ok(members);
//	}

	@GetMapping("/members")
	public ResponseEntity<Map<String, Object>> getAllMembers(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		Map<String, Object> membersWithPagination = adminService.getAllMembersWithPagination(page, size);
		return ResponseEntity.ok(membersWithPagination);
	}


	// 특정 회원 정보 조회
	@GetMapping("/member/{member_id}")
	public ResponseEntity<MemberDTO> getMemberById(@PathVariable String member_id) {
		Optional<MemberDTO> member = adminService.getMemberById(member_id);
		return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
