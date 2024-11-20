package com.webrtc.member.service;

import com.webrtc.member.dao.MemberDAO;
import com.webrtc.member.model.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberDAO memberDAO;  // MyBatis Mapper를 주입받음

    // 이메일로 회원 정보 조회
    public MemberDTO findMemberEmail(String email) {
        return memberDAO.findByEmail(email).orElse(null);  // 이메일로 검색하여 없으면 null 반환
    }


    // 회원 가입 처리
    public void saveMember(MemberDTO memberDTO) {
        // 회원 정보를 MyBatis를 통해 저장
        memberDAO.saveMember(memberDTO);  // MyBatis를 사용하여 DB에 저장
    }

}