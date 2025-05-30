package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.but_eo.dto.ChatMember;
import org.example.but_eo.dto.ChattingDTO;
import org.example.but_eo.entity.*;
import org.example.but_eo.repository.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRepository chattingRepository;
    private final UsersRepository usersRepository;
    private final ChattingMemberRepository chattingMemberRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final RedisChatService redisChatService;

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
            ChattingDTO chattingDTO = new ChattingDTO();
            chattingDTO.setRoomId(room.getChatting().getChatId());
            chattingDTO.setRoomName(room.getChatting().getTitle());

            List<String> message = redisChatService.getLastMessages(room.getChatting().getChatId());
            if (message != null) {
                chattingDTO.setLastMessage(message.get(0));
                chattingDTO.setLastMessageTime(LastMessageTimeFormat(LocalDateTime.parse(message.get(1))));
            } else {
                Optional<ChattingMessage> lastMsgOpt = chattingMessageRepository.findLastMessageByChatIdNative(room.getChatting().getChatId());
                if (lastMsgOpt.isEmpty()) {
                    chattingDTO.setLastMessage(null);
                    chattingDTO.setLastMessageTime(null);
                } else {
                    ChattingMessage lastMsg = lastMsgOpt.get();
                    chattingDTO.setLastMessage(lastMsg.getMessage());
                    chattingDTO.setLastMessageTime(LastMessageTimeFormat(lastMsg.getCreatedAt()));
                }
            }

            ChattingDtoList.add(chattingDTO);
        }

        return ChattingDtoList;
    }

    private String LastMessageTimeFormat(LocalDateTime lastMessageTime) {
        if (lastMessageTime == null) return null;

        Duration duration = Duration.between(lastMessageTime, LocalDateTime.now());

        if (duration.toHours() >= 48) {
            return lastMessageTime.format(DateTimeFormatter.ofPattern("MM.dd"));
        } else if (duration.toHours() >= 24) {
            return "어제";
        } else {
            int hour = lastMessageTime.getHour();
            String minute = String.format("%02d", lastMessageTime.getMinute());
            if (hour >= 12) {
                return "오후 " + (hour % 12 == 0 ? 12 : hour % 12) + ":" + minute;
            } else  {
                return "오전 " + hour + ":" + minute;
            }
        }
    }

    public void exitChatRoom(String chatRoomId, String userId) {
        chattingMemberRepository.deleteChattingMember(chatRoomId, userId);
        log.warn("채팅방 나가기 쿼리문 실행(완)");
    }

    public List<ChatMember> getChatMembers(String roomId) {
        List<ChatMember> memberList = new ArrayList<>();
        List<ChattingMember> membersData = chattingMemberRepository.findByChatMemberList(roomId);
        for (ChattingMember memberData : membersData) {
            ChatMember member = new ChatMember();
            member.setNickName(memberData.getUser().getName());
            member.setProfile(memberData.getUser().getProfile());
            memberList.add(member);
        }
        return memberList;
    }

    public String getNickName(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        return user.getName();
    }
}
