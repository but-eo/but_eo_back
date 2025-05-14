package org.example.but_eo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.but_eo.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, String> {

    @Query(value = "SELECT * FROM chatting_message WHERE chat_id = :chatId ORDER BY create_at ASC", nativeQuery = true)
    List<ChattingMessage> findMessagesByChatIdNative(@Param("chatId") String chatId);

    @Query(value = "SELECT * FROM chatting_message WHERE chat_id = :chatId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<ChattingMessage> findLastMessageByChatIdNative(@Param("chatId") String chatId);
}
