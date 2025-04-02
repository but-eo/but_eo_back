package org.example.but_eo.repository;

import org.example.but_eo.entity.ChattingMember;
import org.example.but_eo.entity.ChattingMemberKey;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingMemberRepository extends JpaRepository<ChattingMember, ChattingMemberKey> {
    void deleteAllByUser(Users user);
}
