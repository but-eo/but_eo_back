package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ChatMessage;
import org.example.but_eo.entity.ChatRoom;
import org.example.but_eo.dto.CreateChatRoomRequest;
import org.example.but_eo.repository.ChattingMemberRepository;
import org.example.but_eo.service.ChatRoomService;
import org.example.but_eo.service.ChattingMessageService;
import org.example.but_eo.service.RedisChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
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
                    "/queue/history",    // 클라이언트가 구독할 주소
                    history
            );

        } else if (message.getType() == ChatMessage.MessageType.EXIT) { // 메세지 타입이 퇴장일 경우
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다"); // 개발 단계에서만 보이게끔
        }

        redisChatService.saveMessageToRedis(message.getChat_id(), message);

        messagingTemplate.convertAndSend("/all/chat/" + message.getChat_id(), message);
    }

    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(request.getUserHashId(), request.getChatRoomName());
        return ResponseEntity.ok(chatRoom);
    }
}
