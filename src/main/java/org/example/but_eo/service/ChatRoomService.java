package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.entity.ChatRoom;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.ChatRoomRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersService usersService;
    private final UsersRepository usersRepository;

    //채팅방 생성
    public ChatRoom createChatRoom(List<String> userIds, String chatRoomName) {
        ChatRoom chatRoom = new ChatRoom();
        String chatRoomHashId = UUID.randomUUID().toString();
        chatRoom.setChatRoomId(chatRoomHashId);
        chatRoom.setChatRoomName(chatRoomName);

        List<Users> users = usersRepository.findByUserHashIdIn(userIds);
        chatRoom.setUsers(users);

        System.out.println("채팅방 생성됨: [" + chatRoomName + "], 유저 IDs: " + userIds);
        return chatRoomRepository.save(chatRoom);
    }

}
