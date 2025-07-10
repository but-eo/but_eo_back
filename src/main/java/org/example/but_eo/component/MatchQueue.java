package org.example.but_eo.component;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.RequestAutoMatch;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchQueue {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private static final String MATCH_QUEUE_KEY = "match_queue";

    public void enqueue(RequestAutoMatch request) {
        redisTemplate.opsForList().rightPush(MATCH_QUEUE_KEY, request);
        eventPublisher.publishEvent(new MatchQueueEvent());
    }

    public Optional<List<RequestAutoMatch>> tryMatch() {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        Long size = ops.size(MATCH_QUEUE_KEY);

        if (size != null && size >= 2) {
            RequestAutoMatch a = (RequestAutoMatch) ops.leftPop(MATCH_QUEUE_KEY);
            RequestAutoMatch b = (RequestAutoMatch) ops.leftPop(MATCH_QUEUE_KEY);

            if (a != null && b != null && a.getSportType().equals(b.getSportType()) && a.getRegion().equals(b.getRegion())) {
                return Optional.of(List.of(a, b));
            }
            if (b != null) ops.leftPush(MATCH_QUEUE_KEY, b);
        }
        return Optional.empty();
    }
}
