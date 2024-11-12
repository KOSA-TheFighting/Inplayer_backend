package com.webrtc.signaling.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.webrtc.signaling.model.vo.ChatMessageVO;

@Mapper
public interface ChatMessageRepository {
	int saveMessage(List<ChatMessageVO> entities);
}
