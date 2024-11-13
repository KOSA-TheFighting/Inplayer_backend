package com.webrtc.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class SortUtil {
	public List<Entry<String, Integer>> sortRoomIdsByUserCountDesc(Map<String, Integer> roomUserCountMap){
		//실시간 시청자수 높은순으로 방송 리스트 가져오기
		List<Map.Entry<String, Integer>> sortedList = roomUserCountMap.entrySet().stream()
				.sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
				.collect(Collectors.toList());

		System.out.println("시청자 높은순 정렬: " + sortedList);
		return sortedList;
	}

	public List<Entry<String, Integer>> sortRoomIdsByUserCountAsc(Map<String, Integer> roomUserCountMap){
		//실시간 시청자수 낮은순으로 방송 리스트 가져오기
		List<Map.Entry<String, Integer>> sortedList = roomUserCountMap.entrySet().stream()
				.sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
				.collect(Collectors.toList());

		System.out.println("시청자 낮은순 정렬: " + sortedList);
		return sortedList;
	}

	public List<Entry<String, String>> sortRoomIdsByLatest(Map<String, String> roomStartTimeMap){
		//방송 시작시간 최신순으로 방송 리스트 가져오기
		List<Map.Entry<String, String>> sortedList = roomStartTimeMap.entrySet().stream()
				.sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
				.collect(Collectors.toList());

		System.out.println("방송 시작시간 최신순 정렬: " + sortedList);
		return sortedList;
	}
}
