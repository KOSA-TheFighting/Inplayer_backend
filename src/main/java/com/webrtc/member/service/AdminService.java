package com.webrtc.member.service;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

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
    public void updateMemberStatus(String member_id, int newStatus) {
        if (newStatus != 0 && newStatus != 1) {
            throw new IllegalArgumentException("잘못된 상태 값: " + newStatus);
        }
        adminRepository.updateMemberStatus(member_id, newStatus);
    }

   // 모든 회원 정보 조회
//    public List<MemberDTO> getAllMembers() {
//        log.info("모든 회원 정보 조회 요청");
//        return adminRepository.findAllMembers();
//    }

   public Map<String, Object> getAllMembersWithPagination(int page, int size) {
       int offset = (page - 1) * size;
       List<MemberDTO> members = adminRepository.findAllMembersWithPagination(size, offset);
       int totalMembers = adminRepository.countAllMembers();
       int totalPages = (int) Math.ceil((double) totalMembers / size);

       Map<String, Object> response = new HashMap<>();
       response.put("members", members);
       response.put("totalPages", totalPages);

       return response;
   }

    // 특정 회원 정보 조회
    public Optional<MemberDTO> getMemberById(String member_id) {
        log.info("회원 정보 조회 요청: {}", member_id);
        return adminRepository.findMemberById(member_id);
    }

}
