package com.webrtc.signaling.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webrtc.page.PageRequestDTO;
import com.webrtc.page.PageResponseDTO;
import com.webrtc.signaling.config.GlobalVariables;
import com.webrtc.signaling.model.dto.StreamDTO;
import com.webrtc.signaling.model.vo.ChatRoomVO;
import com.webrtc.signaling.model.vo.StreamVO;
import com.webrtc.signaling.repository.StreamRepository;
import com.webrtc.util.SortUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {
	private final GlobalVariables globalVariables;
	private final StreamRepository streamRepository;
	private final SortUtil sortUtil;

	public PageResponseDTO<StreamDTO> getList(PageRequestDTO pageRequestDTO) {
		if(pageRequestDTO.getSortBy() == "recommendation") {
			//인기순 정렬
			sortUtil.sortRoomIdsByUserCountDesc(globalVariables.getCheckRoomIdCount());
			//정렬된 id순으로 방송목록 가져오기 구현해야함
		} else if(pageRequestDTO.getSortBy() == "newest") {

			Map<String, String> roomStartTimeMap = new HashMap<>();
			Map<String, StreamDTO> streamInfoMap = globalVariables.getStreamInfo();
			
			//StreamDTO에서 stream_start_time추출해서 roomStartTimeMap에 넣기
			for (Map.Entry<String, StreamDTO> entry : streamInfoMap.entrySet()) {
				String memberId = entry.getKey();
				StreamDTO streamDTO = entry.getValue();
				String stream_start_time = streamDTO.getStream_start_time();
				roomStartTimeMap.put(memberId, stream_start_time);
			}

			//최신순 정렬
			sortUtil.sortRoomIdsByLatest(roomStartTimeMap);
			//정렬된 id순으로 방송목록 가져오기 구현해야함
		} else {
			System.out.println("잘못된 정렬 요청입니다.");
		}

		List<StreamDTO> list = streamRepository.getList(pageRequestDTO);

		return new PageResponseDTO<StreamDTO>(pageRequestDTO, list, streamRepository.getTotalCount(pageRequestDTO));
	}

	@Transactional
	public int registerStream(StreamVO streamVO, String chatroom_status) {
		//DB에 스트림 정보 등록
		final int stream_id = streamRepository.registerStream(streamVO);

		//DB에 채팅방 정보 등록
		ChatRoomVO chatRoomVO = new ChatRoomVO();
		chatRoomVO.setStream_id(stream_id);
		chatRoomVO.setChatroom_status(chatroom_status);
		final int chatroom_id = streamRepository.registerChatRoom(chatRoomVO);

		System.out.println("채팅방ID: " + chatroom_id);
		System.out.println("채팅방VO: " + chatRoomVO);

		return stream_id;
	}
}
