package com.webrtc.signaling.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@Slf4j
@Component
public class WebSocketEventListener {

    @Autowired
    private GlobalVariables globalVariables;

    @EventListener
    public void handleWebsocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        Map<String, List<String>> nativeHeaders = getNativeHeaders(event);
        String roomId = nativeHeaders.get("roomId").get(0);
        String camKey = nativeHeaders.get("camKey").get(0);

        //전역 함수에서 checkRoomId map을 가져와, 해당 세션 Id에 대한 룸 Id 가 있는지 확인
        if(!globalVariables.getCheckRoomId().containsKey(sessionId)){
            //없다는 추가 해준다.
            globalVariables.getCheckRoomId().put(sessionId, roomId);
        }

        //전역 함수에서 checkRoomIdCount map 를 가져와, 해당 룸 Id에 대한 유저수가 있는지 확인
        if(globalVariables.getCheckRoomIdCount().containsKey(roomId)){
            //있다면 유저수를 +1 해준다.
            globalVariables.getCheckRoomIdCount().put(roomId, globalVariables.getCheckRoomIdCount().get(roomId) +1);
        }
        else{
            //아니면 1로 추가해준다.
            globalVariables.getCheckRoomIdCount().put(roomId, 1);
        }

        //전역 함수에서 checkCamKey map 를 가져와, 해당 세션 Id에 대한 camKey가 있는지 확인
        if(!globalVariables.getCheckCamKey().containsKey(sessionId)){
            //없다면 추가해준다.
            globalVariables.getCheckCamKey().put(sessionId, camKey);
        }

        log.info("\n웹소켓 접속 : " + sessionId + "\n"
                + "룸 ID : " + roomId + "\n"
                + "룸 인원 : " + globalVariables.getCheckRoomIdCount().get(roomId));
        System.out.println("연결시 " + globalVariables);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        String roomId = globalVariables.getCheckRoomId().get(sessionId);

        //전역 함수에서 checkRoomIdCount map 을 가져와 해당 룸이 있는지 확인
        if(globalVariables.getCheckRoomIdCount().containsKey(roomId)){
            if(globalVariables.getCheckRoomIdCount().get(roomId) - 1 <= 0){
                //만약 해당 roomId의 유저가 0 이하라면 삭제한다.
                globalVariables.getCheckRoomIdCount().remove(roomId);
            }
            else{
                //아니면 해당 roomId의 유저를 -1 해준다.
                globalVariables.getCheckRoomIdCount().put(roomId, globalVariables.getCheckRoomIdCount().get(roomId) -1);
            }
        }
        
        log.info("\n웹소켓 끊김 : "+sessionId+"\n"
                +"룸 ID : "+roomId + "\n"
                +"룸 인원 : "+ globalVariables.getCheckRoomIdCount().get(roomId) );
        System.out.println("해제시 " + globalVariables);
    }

    //SessionConnectedEvent 에서 NativeHeader 찾기 메서드
    @SuppressWarnings("unchecked")
	private Map<String, List<String>> getNativeHeaders(SessionConnectedEvent event){
    	System.out.println("이벤트 객체1: " + event);
    	
        //messageHeaders 를 추출
        MessageHeaders headers = event.getMessage().getHeaders();
        
        //simpConnectMessage 를 추출
        GenericMessage<?> simpConnectMessage = (GenericMessage<?>) headers.get("simpConnectMessage");
        
        //simpConnectMessage 의 MessageHeader 를 추출
        MessageHeaders simpHeaders = Objects.requireNonNull(simpConnectMessage).getHeaders();
        
        //Map<String, List<String>>로 nativeHeader를 추출하여 리턴한다.
        return (Map<String, List<String>>) simpHeaders.get("nativeHeaders");
    }
}
