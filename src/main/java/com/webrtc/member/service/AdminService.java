package com.webrtc.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.webrtc.member.repository.AdminRepository;
import com.webrtc.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    // 회원 탈퇴 처리
    public void deactivateMember(String member_id) {
        log.info("회원 탈퇴 요청: {}", member_id);
        adminRepository.deactivateMember(member_id);
        log.info("회원 탈퇴 처리 완료: {}", member_id);
    }

    // 모든 회원 정보 조회
    public List<MemberDTO> getAllMembers() {
        log.info("모든 회원 정보 조회 요청");
        return adminRepository.findAllMembers();
    }

    // 특정 회원 정보 조회
    public Optional<MemberDTO> getMemberById(String member_id) {
        log.info("회원 정보 조회 요청: {}", member_id);
        return adminRepository.findMemberById(member_id);
    }

}
