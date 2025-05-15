package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ChattingDTO;
import org.example.but_eo.dto.UserDto;
import org.example.but_eo.entity.*;
import org.example.but_eo.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRepository chattingRepository;
    private final UsersRepository usersRepository;
    private final ChattingMemberRepository chattingMemberRepository;
    private final ChattingMessageRepository chattingMessageRepository;

    //채팅방 생성
    public Chatting createChatRoom(List<String> userIds, String chatRoomName) {
        Chatting chatRoom = new Chatting();
        chatRoom.setChatId(UUID.randomUUID().toString());
        chatRoom.setTitle(chatRoomName);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setState(Chatting.State.PUBLIC);
        chattingRepository.save(chatRoom);
        chattingMemberRepository.flush();
        for (String userId : userIds) {
            Users user = usersRepository.findByUserHashId(userId);
            ChattingMember chattingMember = new ChattingMember();
            chattingMember.setChatting(chatRoom);
            chattingMember.setUser(user);
            chattingMember.setReadCheck(false);
            chattingMember.setChattingMemberKey(new ChattingMemberKey(user.getUserHashId(), chatRoom.getChatId()));
            chattingMemberRepository.save(chattingMember);
        }

        System.out.println("채팅방 생성됨: [ + 채팅방 아이디 : " +  chatRoom.getChatId() + ", 채팅방 이름 : " + chatRoomName + "], 유저 IDs: " + userIds);

        return chatRoom;
    }

    public List<ChattingDTO> searchChatRooms(String userId) {
        List<ChattingMember> rooms = chattingMemberRepository.findByUserHashId(userId);
        System.out.println(rooms);
        List<ChattingDTO> ChattingDtoList = new ArrayList<>();



        for (ChattingMember room : rooms) {
            List<UserDto> userDtoList = new ArrayList<>();
            ChattingDTO chattingDTO = new ChattingDTO();
            chattingDTO.setRoomId(room.getChatting().getChatId());
            chattingDTO.setRoomName(room.getChatting().getTitle());

            List<ChattingMember> members = chattingMemberRepository.findByChatMemberList(room.getChatting().getChatId());

            for (ChattingMember member : members) {
                UserDto userDto = new UserDto();
                userDto.setUserId(member.getUser().getUserHashId());
                userDto.setNickName(member.getUser().getName());
                userDto.setProfile(member.getUser().getProfile());
                userDtoList.add(userDto);
            }
            chattingDTO.setUsers(userDtoList);

            Optional<ChattingMessage> lastMsgOpt = chattingMessageRepository.findLastMessageByChatIdNative(room.getChatting().getChatId());

            if (lastMsgOpt.isEmpty()) {
                chattingDTO.setLastMessage(null);
                chattingDTO.setLastMessageTime(null);
            } else {
                ChattingMessage lastMsg = lastMsgOpt.get();
                chattingDTO.setLastMessage(lastMsg.getMessage());
                chattingDTO.setLastMessageTime(lastMsg.getCreatedAt());
            }

            ChattingDtoList.add(chattingDTO);
        }

        return ChattingDtoList;
    }
}
