package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ChatMessage;
import org.example.but_eo.entity.ChattingMessage;
import org.example.but_eo.repository.ChattingMessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

    private final ChattingMessageRepository chattingMessageRepository;

    public List<ChatMessage> findByMessages(String chat_id) {
        List<ChatMessage> messageList = new ArrayList<>();
        for(ChattingMessage chattingMessage : chattingMessageRepository.findMessagesByChatIdNative(chat_id)) {
            ChatMessage message = new ChatMessage();
            message.setMessageId(chattingMessage.getMessageId());
            message.setChat_id(chattingMessage.getChattingMember().getChatting().getChatId());
            message.setSender(chattingMessage.getChattingMember().getUser().getUserHashId());
            message.setNickName(chattingMessage.getChattingMember().getUser().getName());
            message.setMessage(chattingMessage.getMessage());
            message.setCreatedAt(chattingMessage.getCreatedAt());
            message.setType(ChatMessage.MessageType.valueOf(chattingMessage.getType().toString()));
            messageList.add(message);
        }
        return messageList;
    }
}
