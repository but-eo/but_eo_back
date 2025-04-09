package org.example.but_eo.repository;

import org.example.but_eo.entity.Board;
import org.example.but_eo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findByCategory(Board.Category category, Pageable pageable);
    void deleteAllByUser(Users user);

    Optional<Board> findById(String boardId);

    Page<Board> findByCategoryAndState(Board.Category category, Board.State state, Pageable pageable);

}
