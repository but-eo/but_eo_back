package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisChatService { // Redis 사용하여 채팅을 임시저장

    private final RedisTemplate<String, Object> redisTemplate;
    //private static final int MAX_CHAT_HISTORY_SIZE = 50;

    public void saveMessageToRedis(String roomId, ChatMessage message) {
        String key = "chatroom:" + roomId;
        redisTemplate.opsForList().leftPush(key, message); // 레디스에 리스트로 채팅 내역을 저장

        // 리스트의 길이 제한 최근 50개의 내역만을 유지함 - 데이터 소실을 방지하기 위해 사용하지 않음
        //redisTemplate.opsForList().trim(key, 0, MAX_CHAT_HISTORY_SIZE - 1);
    }

    public List<ChatMessage> getRecentMessages(String roomId) {
        String key = "chatroom:" + roomId;
        List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);
        return rawList.stream().map(o -> (ChatMessage) o).toList();
    }
}
