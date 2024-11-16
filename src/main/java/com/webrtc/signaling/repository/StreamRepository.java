package com.webrtc.signaling.repository;

import org.apache.ibatis.annotations.Mapper;

import com.webrtc.signaling.model.vo.ChatRoomVO;
import com.webrtc.signaling.model.vo.StreamVO;

@Mapper
public interface StreamRepository {
//	List<StreamDTO> getList(PageRequestDTO pageRequestDTO);
//	int getTotalCount(PageRequestDTO pageRequestDTO);
	
	int registerStream(StreamVO streamVO);
	int registerChatRoom(ChatRoomVO chatRoomVO);
}
