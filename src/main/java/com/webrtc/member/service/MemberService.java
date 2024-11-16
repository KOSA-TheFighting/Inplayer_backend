package com.webrtc.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.webrtc.member.model.dto.MemberDTO;
import com.webrtc.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	
	public Optional<MemberDTO> getInfo(String member_id) {
		Optional<MemberDTO> memberDTO = memberRepository.getInfo(member_id);
	
		return memberDTO;
	}
}
