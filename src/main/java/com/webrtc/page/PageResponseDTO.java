package com.webrtc.page;

import java.util.List;

import lombok.Data;

@Data
public class PageResponseDTO<E> {
	private int    page;
	private int    size = 10;
	private String sortBy;
	private int    total;
	
	private List<E> list;
	
	public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> list, int totalCount) {
		this.page  = pageRequestDTO.getPage();
		this.size  = pageRequestDTO.getSize();
		this.list  = list;
	}
}
