package org.example.but_eo.repository;

import org.example.but_eo.entity.Board;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    void deleteAllByUser(Users user);
}
