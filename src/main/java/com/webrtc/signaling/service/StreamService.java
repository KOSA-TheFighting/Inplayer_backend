package com.webrtc.signaling.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webrtc.page.PageRequestDTO;
import com.webrtc.page.PageResponseDTO;
import com.webrtc.signaling.model.dto.StreamDTO;
import com.webrtc.signaling.model.vo.StreamVO;
import com.webrtc.signaling.repository.StreamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {
	private final StreamRepository streamRepository;
	
	public PageResponseDTO<StreamDTO> getList(PageRequestDTO pageRequestDTO) {
		List<StreamDTO> list = streamRepository.getList(pageRequestDTO);

		return new PageResponseDTO<StreamDTO>(pageRequestDTO, list, streamRepository.getTotalCount(pageRequestDTO));
	}

	public int registerStream(StreamVO streamVO) {
		final int stream_id = streamRepository.registerStream(streamVO);
		return stream_id;
	}
}
