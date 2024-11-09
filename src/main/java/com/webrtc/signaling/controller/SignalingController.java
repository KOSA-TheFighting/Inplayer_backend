package com.webrtc.signaling.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import com.webrtc.signaling.dto.*;

@Slf4j
@RestController
public class SignalingController {
    private final ConcurrentHashMap<String, String> roomBroadcasters = new ConcurrentHashMap<>();

    @MessageMapping("/peer/offer/{camKey}/{roomId}")
    @SendTo("/topic/peer/offer/{camKey}/{roomId}")
    public String PeerHandleOffer(@Payload String offer, 
                                @DestinationVariable String roomId,
                                @DestinationVariable String camKey) {
        log.info("[OFFER] Room: {}, From: {}, Offer: {}", roomId, camKey, offer);
        return offer;
    }

    @MessageMapping("/peer/iceCandidate/{camKey}/{roomId}")
    @SendTo("/topic/peer/iceCandidate/{camKey}/{roomId}")
    public String PeerHandleIceCandidate(@Payload String candidate, 
                                      @DestinationVariable String roomId,
                                      @DestinationVariable String camKey) {
        log.info("[ICE] Room: {}, From: {}", roomId, camKey);
        return candidate;
    }

    @MessageMapping("/peer/answer/{camKey}/{roomId}")
    @SendTo("/topic/peer/answer/{camKey}/{roomId}")
    public String PeerHandleAnswer(@Payload String answer, 
                                @DestinationVariable String roomId,
                                @DestinationVariable String camKey) {
        log.info("[ANSWER] Room: {}, From: {}", roomId, camKey);
        return answer;
    }

    @MessageMapping("/call/key")
    @SendTo("/topic/call/key")
    public String callKey(@Payload String message) {
        log.info("[KEY CALL] Message: {}", message);
        return message;
    }

    @MessageMapping("/send/key")
    @SendTo("/topic/send/key")
    public String sendKey(@Payload String message) {
        log.info("[KEY SEND] Key: {}", message);
        return message;
    }
    //추가된 부분
    @MessageMapping("/room/status/{roomId}")
    @SendTo("/topic/room/status/{roomId}")
    public RoomStatus getRoomStatus(@DestinationVariable String roomId) {
        String broadcasterKey = roomBroadcasters.get(roomId);
        RoomStatus status = new RoomStatus();
        status.setRoomId(roomId);
        status.setHasBroadcaster(broadcasterKey != null);
        status.setBroadcasterKey(broadcasterKey);
        log.info("[ROOM STATUS] Room: {}, Has Broadcaster: {}, Broadcaster Key: {}", 
                roomId, status.isHasBroadcaster(), broadcasterKey);
        return status;
    }

    @MessageMapping("/room/broadcast/start/{roomId}")
    @SendTo("/topic/room/status/{roomId}")
    public RoomStatus startBroadcast(@DestinationVariable String roomId, 
                                  @Payload BroadcastStartRequest request) {
        String broadcasterKey = request.getBroadcasterKey();
        roomBroadcasters.put(roomId, broadcasterKey);
        
        RoomStatus status = new RoomStatus();
        status.setRoomId(roomId);
        status.setHasBroadcaster(true);
        status.setBroadcasterKey(broadcasterKey);
        
        log.info("[BROADCAST START] Room: {}, Broadcaster: {}", roomId, broadcasterKey);
        return status;
    }

    @MessageMapping("/room/broadcast/end/{roomId}")
    @SendTo("/topic/room/status/{roomId}")
    public RoomStatus endBroadcast(@DestinationVariable String roomId) {
        String removedBroadcaster = roomBroadcasters.remove(roomId);
        
        RoomStatus status = new RoomStatus();
        status.setRoomId(roomId);
        status.setHasBroadcaster(false);
        
        log.info("[BROADCAST END] Room: {}, Removed Broadcaster: {}", roomId, removedBroadcaster);
        return status;
    }

}