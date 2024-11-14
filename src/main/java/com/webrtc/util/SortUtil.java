package com.webrtc.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.webrtc.page.PageRequestDTO;

@Component
public class SortUtil {
	public List<Entry<String, Integer>> sortRoomIdsByUserCountDesc(Map<String, Integer> roomUserCountMap, PageRequestDTO pageRequestDTO){
		//실시간 시청자수 높은순으로 방송 리스트 가져오기
		List<Map.Entry<String, Integer>> sortedList = roomUserCountMap.entrySet().stream()
				.sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
				.collect(Collectors.toList());

		System.out.println("시청자 높은순 정렬: " + sortedList);
		return getPagedResult(sortedList, pageRequestDTO.getPage(), pageRequestDTO.getSize());
	}

	public List<Entry<String, Integer>> sortRoomIdsByUserCountAsc(Map<String, Integer> roomUserCountMap, PageRequestDTO pageRequestDTO){
		//실시간 시청자수 낮은순으로 방송 리스트 가져오기
		List<Map.Entry<String, Integer>> sortedList = roomUserCountMap.entrySet().stream()
				.sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
				.collect(Collectors.toList());

		System.out.println("시청자 낮은순 정렬: " + sortedList);
		return getPagedResult(sortedList, pageRequestDTO.getPage(), pageRequestDTO.getSize());
	}

	public List<Entry<String, String>> sortRoomIdsByLatest(Map<String, String> roomStartTimeMap, PageRequestDTO pageRequestDTO){
		//방송 시작시간 최신순으로 방송 리스트 가져오기
		List<Map.Entry<String, String>> sortedList = roomStartTimeMap.entrySet().stream()
				.sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
				.collect(Collectors.toList());

		System.out.println("방송 시작시간 최신순 정렬: " + sortedList);
		return getPagedResult(sortedList, pageRequestDTO.getPage(), pageRequestDTO.getSize());
	}

	public <T> List<T> getPagedResult(List<T> sortedList, int page, int size) {
		// 페이지는 1부터 시작
		int startIndex = (page -1) * size;
		int endIndex = Math.min(startIndex + size, sortedList.size());

		// 페이지가 범위를 벗어난 경우 빈 리스트 반환
		if (startIndex >= sortedList.size()) {
			return Collections.emptyList();
		}
		return sortedList.subList(startIndex, endIndex);
	}
}
