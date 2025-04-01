package org.example.but_eo.repository;

import org.example.but_eo.entity.TeamMember;
import org.example.but_eo.entity.TeamMemberKey;
import org.example.but_eo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberKey> {

    @Query("""
        SELECT COUNT(tm) > 0
        FROM TeamMember tm
        WHERE tm.user.userHashId = :userId
        AND tm.team.event = :event
    """)
    boolean existsByUserAndEvent(@Param("userId") String userId, @Param("event") Team.Event event);
}
