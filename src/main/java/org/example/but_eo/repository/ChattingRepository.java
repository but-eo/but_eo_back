package org.example.but_eo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.but_eo.entity.Chatting;
import org.example.but_eo.entity.ChattingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, String> {
}
