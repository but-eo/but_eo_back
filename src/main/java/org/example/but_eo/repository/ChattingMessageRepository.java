package org.example.but_eo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.but_eo.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, String> {

    @Query(value = "SELECT * FROM chatting_message WHERE chat_id = :chatId ORDER BY create_at ASC", nativeQuery = true)
    List<ChattingMessage> findMessagesByChatIdNative(@Param("chatId") String chatId);
}
