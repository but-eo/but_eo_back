package org.example.but_eo.repository;

import org.example.but_eo.entity.Board;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findByCategory(Board.Category category, Pageable pageable);
    void deleteAllByUser(Users user);
}
