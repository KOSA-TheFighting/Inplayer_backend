package com.webrtc.signaling.dto;

import lombok.Data;

@Data
public class RoomStatus {
    private String roomId;
    private boolean hasBroadcaster;
    private String broadcasterKey;
}
