package com.webrtc.member.repository;

import java.util.Optional;
import com.webrtc.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemberRepository {
	Optional<MemberDTO> getInfo(String member_id);

	// 이메일로 회원 조회
	Optional<MemberDTO> findByEmail(String email);

	// 회원 가입 처리
	void saveMember(MemberDTO memberDTO);
}
