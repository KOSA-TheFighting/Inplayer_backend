package com.webrtc.signaling.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webrtc.page.PageRequestDTO;
import com.webrtc.page.PageResponseDTO;
import com.webrtc.signaling.config.GlobalVariables;
import com.webrtc.signaling.config.SampleData;
import com.webrtc.signaling.model.dto.StreamDTO;
import com.webrtc.signaling.model.vo.StreamVO;
import com.webrtc.signaling.service.StreamService;
import com.webrtc.util.MapperUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stream")
public class StreamController {
	private final GlobalVariables globalVariables;
	private final MapperUtil mapperUtil;
	private final StreamService streamService;
	//샘플데이터용
	private final SampleData sampleData;
	
	@GetMapping("list")
	public ResponseEntity<Map<String, Object>> getStreamList(PageRequestDTO pageRequestDTO) {
		System.out.println("방송목록 요청 목록: " + pageRequestDTO);
		System.out.println("샘플용 랜덤시간 리스트: " + sampleData.getDateTime());

		for (int i = 1; i <= 123; i++) {
			StreamDTO streamDTO = new StreamDTO();
			streamDTO.setMember_id("member" + i);
			streamDTO.setMember_nickname("nickname" + i);

			streamDTO.setStream_id(i);
			streamDTO.setStreamtag_num("2000");
			streamDTO.setStreamtag_name("talk");
			streamDTO.setStream_title("title" + i);
			streamDTO.setStream_description("description" + i);
			
			streamDTO.setStream_start_time(sampleData.getDateTime().get(i-1));
			streamDTO.setStream_status("live");

			streamDTO.setStream_view_count(0);
			streamDTO.setChatroom_status("active");
			streamDTO.setStream_realtime_viewer_count(i*10);
			globalVariables.getStreamInfo().put("member" + i, streamDTO);
			globalVariables.getCheckRoomIdCount().put(streamDTO.getMember_id(), streamDTO.getStream_realtime_viewer_count());
		}
		System.out.println("전체 방송 목록: " + globalVariables.getStreamInfo());

		PageResponseDTO<StreamDTO> pageResponseDTO = streamService.getList(pageRequestDTO);
		System.out.println("방송목록 응답 목록: " + pageResponseDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("pageResponse", pageResponseDTO);
		response.put("pageRequest", pageRequestDTO);

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("register")
	public ResponseEntity<Map<String, Object>> registerStream(StreamDTO streamDTO){
		String chatroom_status = streamDTO.getChatroom_status();
		//방송 정보 DB에 저장
		int stream_id = streamService.registerStream(mapperUtil.map(streamDTO, StreamVO.class), chatroom_status);
		streamDTO.setStream_id(stream_id);

		//방송 시작시 방송 정보를 map형태로 저장
		globalVariables.getStreamInfo().put(streamDTO.getMember_id(), streamDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("stream_id", stream_id);
		response.put("status", "success");
		System.out.println("방송 등록 응답 목록: " + response);

		return ResponseEntity.ok(response);
	}
}
