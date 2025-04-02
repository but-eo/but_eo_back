package org.example.but_eo.repository;

import org.example.but_eo.entity.Comment;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    void deleteAllByUser(Users user);
}

