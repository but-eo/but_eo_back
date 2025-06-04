package org.example.but_eo.repository;

import org.example.but_eo.entity.TeamMember;
import org.example.but_eo.entity.TeamMemberKey;
import org.example.but_eo.entity.Team;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberKey> {

    @Query("""
        SELECT COUNT(tm) > 0
        FROM TeamMember tm
        WHERE tm.user.userHashId = :userId
        AND tm.team.event = :event
    """)

    boolean existsByUserAndEvent(@Param("userId") String userId, @Param("event") Team.Event event);

    void deleteAllByUser(Users user);

    long countByTeam_TeamId(String teamId);

    List<TeamMember> findAllByUser(Users user);

    Optional<TeamMember> findByUser_UserHashId(String userId);

    Optional<TeamMember> findByUser_UserHashIdAndType(String userId, TeamMember.Type type);

    List<TeamMember> findAllByUser_UserHashIdAndType(String userHashId, TeamMember.Type type);

    Optional<TeamMember> findByUser_UserHashIdAndTypeAndTeam_Event(
            String userHashId,
            TeamMember.Type type,
            Team.Event event
    );

}
