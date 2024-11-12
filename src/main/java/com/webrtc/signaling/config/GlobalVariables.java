package com.webrtc.signaling.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class GlobalVariables {
    //룸 ID 확인 map ( sessionId, roomId )
    private Map<String, String> checkRoomId = new HashMap<>();
    
    //룸 ID 인원 확인 map ( roomId, roomUserCount )
    private Map<String, Integer> checkRoomIdCount = new HashMap<>();
    
    //유저 camKey 확인 map ( sessionId, camKey )
    private Map<String, String> checkCamKey = new HashMap<>();
}
