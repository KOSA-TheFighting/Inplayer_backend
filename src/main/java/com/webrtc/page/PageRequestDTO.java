package com.webrtc.page;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PageRequestDTO {
	private int    page = 1;
	private int    size = 10;
	private String sortBy;
	private String search;

	public int getSkip() {
		return (page - 1) * size;
	}
}
