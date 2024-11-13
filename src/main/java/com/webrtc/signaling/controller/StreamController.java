package com.webrtc.signaling.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webrtc.page.PageRequestDTO;
import com.webrtc.page.PageResponseDTO;
import com.webrtc.signaling.config.GlobalVariables;
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

	@GetMapping("list")
	public ResponseEntity<Map<String, Object>> getStreamList(PageRequestDTO pageRequestDTO) {
		System.out.println("방송목록 요청 목록: " + pageRequestDTO);
		PageResponseDTO<StreamDTO> pageResponseDTO = streamService.getList(pageRequestDTO);
		System.out.println("방송목록 응답 목록: " + pageResponseDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("pageResponse", pageResponseDTO);
		response.put("pageRequest", pageRequestDTO);

		return ResponseEntity.ok(response);
	}

	@PostMapping("register")
	public ResponseEntity<Map<String, Object>> registerStream(StreamDTO streamDTO){
		//방송 시작시 방송 정보를 map형태로 저장
		globalVariables.getStreamInfo().put(streamDTO.getMember_id(), streamDTO);
		
		Map<String, Object> response = new HashMap<>();

		streamService.registerStream(mapperUtil.map(streamDTO, StreamVO.class));

		response.put("message", "방송이 등록되었습니다.");
		response.put("status", "success");
		System.out.println("방송 등록 응답 목록: " + response);

		return ResponseEntity.ok(response);
	}
}
