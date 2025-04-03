package org.example.but_eo.controller;

import org.example.but_eo.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat") //flutter에서 전달할 주소
    @SendTo("/all")
    public ChatMessage sendMessage(ChatMessage message){
        return message;
    }
}
