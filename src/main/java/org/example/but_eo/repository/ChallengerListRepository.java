package org.example.but_eo.repository;

import org.example.but_eo.entity.ChallengerKey;
import org.example.but_eo.entity.ChallengerList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengerListRepository extends JpaRepository<ChallengerList, ChallengerKey> {

    List<ChallengerList> findByMatching_MatchId(String matchId);

    void deleteAllByMatching_MatchId(String matchId);

}
