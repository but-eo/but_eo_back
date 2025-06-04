package org.example.but_eo.repository;

import org.example.but_eo.entity.Team;
import org.example.but_eo.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {

    @Query("""
    SELECT t FROM Team t
    LEFT JOIN FETCH t.teamMemberList tm
    LEFT JOIN FETCH tm.user
    WHERE t.teamId = :teamId
""")
    Optional<Team> findWithMembersByTeamId(@Param("teamId") String teamId);

    boolean existsByTeamName(String teamName);
}
