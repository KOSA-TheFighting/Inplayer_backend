package com.webrtc.signaling.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webrtc.signaling.model.dto.ChatMessageDTO;
import com.webrtc.signaling.model.vo.ChatMessageVO;
import com.webrtc.signaling.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final RedisTemplate<String, ChatMessageDTO> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;

    // Redis에 메시지 저장
    public void saveMessage(String roomId, ChatMessageDTO message) {
        String redisKey = "방번호: " + roomId;
        redisTemplate.opsForList().rightPush(redisKey, message);
        
        // Redis에 저장된 메시지 수 확인
        Long size = redisTemplate.opsForList().size(redisKey);
        if (size != null && size >= 5) { //일정 메시지 수가 되면 DB에 저장
            saveMessagesToDB(redisKey);
        }
    }

    // Redis -> MariaDB 저장 로직
    @Transactional
    private void saveMessagesToDB(String redisKey) {
        List<ChatMessageDTO> messages = redisTemplate.opsForList()
            .range(redisKey, 0, -1)
            .stream()
            .map(msg -> (ChatMessageDTO) msg)
            .collect(Collectors.toList());

        if (messages != null && !messages.isEmpty()) {
            // DTO -> Entity 변환
            List<ChatMessageVO> entities = messages.stream()
                .map(this::convertChatMessageDTOtoVO)
                .collect(Collectors.toList());

            // MariaDB에 저장
            int rows = chatMessageRepository.saveMessage(entities);
            System.out.println("DB에 저장된 채팅 메시지수: " + rows);
            
            // Redis에서 삭제
            redisTemplate.delete(redisKey);
        }
    }
    
    //DTO필드명을 VO필드명으로 변환해서 DB와 일치시키는 작업
    private ChatMessageVO convertChatMessageDTOtoVO(ChatMessageDTO chatMessageDTO) {
        return ChatMessageVO.builder()
            .member_id(chatMessageDTO.getSender())
            .chatroom_id(chatMessageDTO.getRoomId())
            .messagetype_num(chatMessageDTO.getType())
            .chatmessage_content(chatMessageDTO.getMessage())
            .chatmessage_sent_time(chatMessageDTO.getTime())
            .build();
    }
}
