package com.webrtc.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.webrtc.member.repository.MemberRepository;
import com.webrtc.member.model.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;// MyBatis Mapper를 주입받음
	
	public Optional<MemberDTO> getInfo(String member_id) {
		Optional<MemberDTO> memberDTO = memberRepository.getInfo(member_id);
		System.out.println("멤버요청: " + member_id);
		System.out.println("멤버정보: " + memberDTO);

		return memberDTO;
	}

	// 이메일로 회원 정보 조회
	public MemberDTO findMemberEmail(String email) {
		return memberRepository.findByEmail(email).orElse(null);  // 이메일로 검색하여 없으면 null 반환
	}


	// 회원 가입 처리
	public void saveMember(MemberDTO memberDTO) {
		// 회원 정보를 MyBatis를 통해 저장
		memberRepository.saveMember(memberDTO);  // MyBatis를 사용하여 DB에 저장
	}

}
