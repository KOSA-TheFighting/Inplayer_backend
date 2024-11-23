package com.webrtc.member.repository;

import java.util.Optional;
import com.webrtc.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface MemberRepository {
	Optional<MemberDTO> getInfo(String member_id);

	// 이메일로 회원 조회
	Optional<MemberDTO> findByEmail(String email);

	// 회원 가입 처리
	void saveMember(MemberDTO memberDTO);

	//닉네임 수정 처리
	void updateNickname(@Param("member_id") String member_id, @Param("member_nickname") String member_nickname);



}
