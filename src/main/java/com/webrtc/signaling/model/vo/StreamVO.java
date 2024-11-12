package com.webrtc.signaling.model.vo;

import lombok.Data;

@Data
public class StreamVO {
	private int    stream_id;
	private String member_id;
	private String streamtag_num;
	private String stream_title;
	private String stream_start_time;
	private String stream_end_time;
	private String stream_description;
	private String stream_status;
	private int    stream_view_count;
}
