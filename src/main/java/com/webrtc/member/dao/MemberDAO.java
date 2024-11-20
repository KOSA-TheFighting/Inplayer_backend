package com.webrtc.member.dao;

import com.webrtc.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberDAO {
    // 이메일로 회원 조회
    Optional<MemberDTO> findByEmail(String email);
    // 회원 가입 처리
    void saveMember(MemberDTO memberDTO);
//    // 회원 ID로 회원 조회
//    Optional<MemberDTO> findByMemberId(String memberId);
//
//    // 닉네임으로 회원 조회
//    Optional<MemberDTO> findByMemberNickname(String nickname);

   // MemberDTO findMemberEmail(String email);
}