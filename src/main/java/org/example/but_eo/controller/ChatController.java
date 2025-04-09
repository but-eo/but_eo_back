package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.but_eo.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("chat/message") // 현재 세팅의 경우 클라이언트에서 보낼 때 /app/chat/message
    public void message(ChatMessage message) {
        message.setCreatedAt(LocalDateTime.now());

        if (message.getType() == ChatMessage.MessageType.ENTER) { // 메세지 타입이 입장일 경우
            message.setContent(message.getSender() + "님이 입장하셨습니다");
        } else if (message.getType() == ChatMessage.MessageType.EXIT) { // 메세지 타입이 퇴장일 경우
            message.setContent(message.getSender() + "님이 퇴장하셨습니다");
        }

        messagingTemplate.convertAndSend("/all/chat/" + message.getRoomId(), message);
    }

//    @MessageMapping("/chat") //flutter에서 전달할 주소
//    @SendTo("/all")
//    public ChatMessage sendMessage(ChatMessage message){
//        return message;
//    }
}
