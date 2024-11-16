package com.webrtc.member.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.webrtc.member.model.dto.MemberDTO;

@Mapper
public interface MemberRepository {
	public Optional<MemberDTO> getInfo(String member_id);

}
