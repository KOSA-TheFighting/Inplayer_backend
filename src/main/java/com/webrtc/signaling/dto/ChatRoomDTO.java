package com.webrtc.signaling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatRoomDTO {
    private String roomId;
    private String roomName;
    private long userCount;
    @Default
    private HashMap<String, String> userList = new HashMap<>();

    public ChatRoomDTO create(String roomName){
        return ChatRoomDTO.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .build();
    }
}
