package com.webrtc.member.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import com.webrtc.member.model.dto.MemberDTO;


@Mapper
public interface AdminRepository {

    // 회원 탈퇴 처리 (member_delete_yn을 1로 업데이트)
    void deactivateMember(String member_id);

    // 모든 회원 정보 조회
    List<MemberDTO> findAllMembers();

    // 특정 회원 정보 조회
    Optional<MemberDTO> findMemberById(String member_id);


}
