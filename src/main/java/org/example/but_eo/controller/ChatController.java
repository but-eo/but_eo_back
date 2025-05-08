package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.entity.ChatRoom;
import org.example.but_eo.dto.CreateChatRoomRequest;
import org.example.but_eo.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;


    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(request.getUserHashId(), request.getChatRoomName());
        return ResponseEntity.ok(chatRoom);
    }

}
