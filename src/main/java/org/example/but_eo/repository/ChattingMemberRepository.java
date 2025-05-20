package org.example.but_eo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.but_eo.entity.Chatting;
import org.example.but_eo.entity.ChattingMember;
import org.example.but_eo.entity.ChattingMemberKey;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingMemberRepository extends JpaRepository<ChattingMember, ChattingMemberKey> {
    void deleteAllByUser(Users user);

    @Query(value = "select * from chatting_member where chat_id = :chatId", nativeQuery = true)
    List<ChattingMember> findByChatMemberList(@Param("chatId") String chatId);

    @Query("SELECT cm FROM ChattingMember cm WHERE cm.chattingMemberKey.userHashId = :userHashId")
    List<ChattingMember> findByUserHashId(@Param("userHashId") String userHashId);
}
