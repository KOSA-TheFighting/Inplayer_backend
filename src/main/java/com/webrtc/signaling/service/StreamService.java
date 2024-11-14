package com.webrtc.signaling.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
		List<StreamDTO> list;

		if(pageRequestDTO.getSortBy() == "recommendation") {
			//map을 인기순 정렬
			List<Map.Entry<String, Integer>> sortedEntries = sortUtil.sortRoomIdsByUserCountDesc(globalVariables.getCheckRoomIdCount());
			Map<String, StreamDTO> streamInfo = globalVariables.getStreamInfo();

			//정렬된 id순으로 방송목록 변환
			list = convertToStreamDTOList(sortedEntries, streamInfo);

		} else if(pageRequestDTO.getSortBy() == "newest") {

			Map<String, StreamDTO> streamInfo = globalVariables.getStreamInfo();

			//최신순으로 방송목록 정렬
			list = streamInfo.values()
					.stream()
					.sorted((s1, s2) -> s2.getStream_start_time().compareTo(s1.getStream_start_time()))
					.collect(Collectors.toList());

		} else {
			list = null;
			System.out.println("잘못된 정렬 요청입니다.");
		}

//		list = streamRepository.getList(pageRequestDTO);

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

	// Map.Entry 리스트를 StreamDTO 리스트로 변환하는 메서드
	private List<StreamDTO> convertToStreamDTOList(List<Map.Entry<String, Integer>> sortedEntries, 
			Map<String, StreamDTO> streamInfo) {
		return sortedEntries.stream()
				.map(entry -> {
					String roomId = entry.getKey();
					Integer viewerCount = entry.getValue();

					// streamInfo에서 해당 roomId를 가진 StreamDTO를 찾아서 반환
					return streamInfo.values()
							.stream()
							.filter(dto -> dto.getMember_id().equals(roomId))
							.map(dto -> {
								dto.setStream_realtime_viewer_count(viewerCount);
								return dto;
							})
							.findFirst()
							.orElse(null);
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}
