package com.webrtc.signaling.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.webrtc.page.PageRequestDTO;
import com.webrtc.signaling.model.dto.StreamDTO;
import com.webrtc.signaling.model.vo.StreamVO;

@Mapper
public interface StreamRepository {
	List<StreamDTO> getList(PageRequestDTO pageRequestDTO);
	int getTotalCount(PageRequestDTO pageRequestDTO);
	
	int registerStream(StreamVO streamVO);
}
