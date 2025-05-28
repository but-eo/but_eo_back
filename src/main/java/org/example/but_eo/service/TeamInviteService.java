package org.example.but_eo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.TeamInviteRequest;
import org.example.but_eo.entity.*;
import org.example.but_eo.repository.TeamInvitationRepository;
import org.example.but_eo.repository.TeamMemberRepository;
import org.example.but_eo.repository.TeamRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamInviteService {

    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamInvitationRepository teamInvitationRepository;

    // 초대 발송
    @Transactional
    public void inviteUserToTeam(String teamId, TeamInviteRequest request, String leaderId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀이 존재하지 않습니다."));

        TeamMember leader = teamMemberRepository.findById(new TeamMemberKey(leaderId, teamId))
                .orElseThrow(() -> new IllegalAccessError("리더 아님"));

        if (leader.getType() != TeamMember.Type.LEADER)
            throw new IllegalAccessError("리더만 초대 가능");

        Users user = usersRepository.findByUserHashId(request.getUserId());
        if (user == null) throw new IllegalArgumentException("유저 없음");

        if (team.getTeamType() == Team.Team_Type.SOLO)
            throw new IllegalStateException("SOLO 팀은 초대 불가");

        boolean alreadyInvited = teamInvitationRepository
                .existsPendingByUserAndTeamAndDirection(user.getUserHashId(), teamId, TeamInvitation.Direction.INVITE);
        if (alreadyInvited) throw new IllegalStateException("이미 초대된 유저입니다.");

        TeamInvitation invitation = TeamInvitation.createInvite(team, user);
        teamInvitationRepository.save(invitation);
    }

    // 초대 취소
    @Transactional
    public void cancelInvite(String teamId, String targetUserId, String leaderId) {
        TeamMember leader = teamMemberRepository.findById(new TeamMemberKey(leaderId, teamId))
                .orElseThrow(() -> new IllegalAccessError("리더 아님"));

        if (leader.getType() != TeamMember.Type.LEADER)
            throw new IllegalAccessError("리더만 가능");

        TeamInvitation invitation = teamInvitationRepository
                .findByUser_UserHashIdAndTeam_TeamIdAndDirectionAndStatus(
                        targetUserId, teamId, TeamInvitation.Direction.INVITE, TeamInvitation.Status.PENDING)
                .orElseThrow(() -> new IllegalArgumentException("초대 없음"));

        teamInvitationRepository.delete(invitation);
    }
}
