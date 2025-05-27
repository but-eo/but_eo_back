package org.example.but_eo.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.but_eo.dto.ChatMessage;
import org.example.but_eo.entity.ChattingMessage;
import org.example.but_eo.repository.ChattingMessageRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHistoryScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChattingMessageRepository chatMessageRepository;

    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분마다 실행
    public void flushRedisToDatabase() {
        Set<String> keys = redisTemplate.keys("chatroom:*");
        if (keys == null) return;

        for (String key : keys) {
            List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);
            if (messages == null || messages.isEmpty()) continue;

            List<ChattingMessage> entities = messages.stream()
                    .map(m -> ((ChattingMessage) m))
                    .toList();

            chatMessageRepository.saveAll(entities);
            redisTemplate.delete(key); // Redis 비우기

            log.info("✅ 저장 완료: {}건, 채널: {}", entities.size(), key);
        }
    }
}