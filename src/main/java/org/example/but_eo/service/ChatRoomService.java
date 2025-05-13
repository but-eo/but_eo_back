package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ChatRoomDTO;
import org.example.but_eo.dto.UserDto;
import org.example.but_eo.entity.ChatRoom;
import org.example.but_eo.entity.ChattingMessage;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.ChatRoomRepository;
import org.example.but_eo.repository.ChattingMessageRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final ChattingMessageRepository chattingMessageRepository;

    //채팅방 생성
    public ChatRoom createChatRoom(List<String> userIds, String chatRoomName) {
        ChatRoom chatRoom = new ChatRoom();
        String chatRoomHashId = UUID.randomUUID().toString();
        chatRoom.setChatRoomId(chatRoomHashId);
        chatRoom.setChatRoomName(chatRoomName);

        List<Users> users = usersRepository.findByUserHashIdIn(userIds);
        chatRoom.setUsers(users);

        System.out.println("채팅방 생성됨: [ + 채팅방 아이디 : " +  chatRoomHashId + ", 채팅방 이름 : " + chatRoomName + "], 유저 IDs: " + userIds);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomDTO> searchChatRooms(String userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByUserHashId(userId);

        return rooms.stream().map(room -> {
            Optional<ChattingMessage> lastMsgOpt = chattingMessageRepository
                    .findLastMessageByChatIdNative(room.getChatRoomId());

            String lastMessage = lastMsgOpt.map(ChattingMessage::getMessage).orElse("");
            LocalDateTime lastMessageTime = lastMsgOpt.map(ChattingMessage::getCreatedAt).orElse(null);

            return new ChatRoomDTO(
                    room.getChatRoomId(),
                    room.getChatRoomName(),
                    room.getUsers().stream()
                            .map(user -> new UserDto(user.getUserHashId(), user.getName(), user.getProfile()))
                            .collect(Collectors.toList()),
                    lastMessage,
                    lastMessageTime
            );
        }).collect(Collectors.toList());
    }
}
