package org.example.but_eo.repository;

import org.example.but_eo.entity.Team;
import org.example.but_eo.entity.TeamInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.example.but_eo.entity.Users;

import java.util.List;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, String> {

    @Query("""
    SELECT COUNT(i) > 0
    FROM TeamInvitation i
    WHERE i.user.userHashId = :userId
    AND i.team.teamId = :teamId
    AND i.status = :status
""")
    boolean existsPendingByUserAndTeam(@Param("userId") String userId,
                                       @Param("teamId") String teamId,
                                       @Param("status") TeamInvitation.Status status);


    List<TeamInvitation> findByUser_UserHashIdAndStatus(String userId, TeamInvitation.Status status);

    void deleteAllByUser(Users user);

    boolean existsByUser_UserHashIdAndTeam_TeamId(String userId, String teamId);

    void deleteAllByTeam(Team team);
}
