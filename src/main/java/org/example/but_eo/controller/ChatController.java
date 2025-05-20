package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.but_eo.dto.ChatMessage;
import org.example.but_eo.dto.ChattingDTO;
import org.example.but_eo.dto.CreateChatRoomRequest;
import org.example.but_eo.entity.Chatting;
import org.example.but_eo.service.ChattingMessageService;
import org.example.but_eo.service.ChattingService;
import org.example.but_eo.service.RedisChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChattingService chattingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisChatService redisChatService;
    private final ChattingMessageService chattingMessageService;

    @MessageMapping("chat/message") // 현재 세팅의 경우 클라이언트에서 보낼 때 /app/chat/message
    public void message(@Payload ChatMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setCreatedAt(LocalDateTime.now());

        if (message.getType() == ChatMessage.MessageType.ENTER) { // 메세지 타입이 입장일 경우
            message.setMessage(message.getSender() + "님이 입장하셨습니다"); // 개발 단계에서만 보이게끔

            // 🔽 과거 메시지 조회
//            List<ChatMessage> history = redisChatService.getRecentMessages(message.getRoomId());

            List<ChatMessage> history = new ArrayList<>();

            history.addAll(chattingMessageService.findByMessages(message.getMessageId()));
            history.addAll(redisChatService.getRecentMessages(message.getChat_id()));

            messagingTemplate.convertAndSendToUser(
                    message.getSender(), // Flutter에서 sender를 유저 고유값으로 설정
                    "/api/chatroom/ " + message.getChat_id(),    // 클라이언트가 구독할 주소
                    history
            );

        } else if (message.getType() == ChatMessage.MessageType.EXIT) { // 메세지 타입이 퇴장일 경우
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다"); // 개발 단계에서만 보이게끔
        }

        redisChatService.saveMessageToRedis(message.getChat_id(), message);

        messagingTemplate.convertAndSend("/all/chat/" + message.getChat_id(), message);
    }

    @PostMapping("/chatrooms")
    public ResponseEntity<Chatting> createChatRoom(@RequestBody CreateChatRoomRequest request, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        request.getUserHashId().add(userId);
        Chatting chatRoom = chattingService.createChatRoom(request.getUserHashId(), request.getChatRoomName());
        return ResponseEntity.ok(chatRoom);
    }

    //유저 아이디 -> 채팅방 조회
    @GetMapping("/searchChatRooms")
    public ResponseEntity<?> searchChatRoom(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();

        if (userId == null) {
            log.warn("인증된 사용자가 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<ChattingDTO> rooms = chattingService.searchChatRooms(userId);
        System.out.println("현재 접속된 유저 아이디 : " + userId);
        System.out.println("현재 접속된 유저 채팅방 리스트 : " + rooms);
        return ResponseEntity.ok(rooms);
    }
}
