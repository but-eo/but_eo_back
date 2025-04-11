package org.example.but_eo.repository;

import org.example.but_eo.entity.MatchReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<MatchReview, String> {
    boolean existsByMatch_MatchIdAndWriter_UserHashId(String matchId, String writerId);
}
