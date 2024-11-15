package com.webrtc.signaling.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SampleData {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private List<String> dateTime = new ArrayList<>();
	
    public SampleData() {
    	randomDateTime();
    }
	
	public void randomDateTime() {
		for(int i = 1; i <= 123; i++) {
			// 샘플용 두 날짜 사이의 랜덤 시간을 생성
			LocalDateTime start = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
			LocalDateTime end = LocalDateTime.of(2024, 11, 14, 23, 59, 59);

			long randomEpochSecond = ThreadLocalRandom.current().nextLong(
					start.toEpochSecond(java.time.ZoneOffset.UTC),
					end.toEpochSecond(java.time.ZoneOffset.UTC)
					);
			LocalDateTime randomDateTime = LocalDateTime.ofEpochSecond(randomEpochSecond, 0, java.time.ZoneOffset.UTC);
			dateTime.add(randomDateTime.format(formatter));
		}
	}
}
