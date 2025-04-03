package org.example.but_eo.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.but_eo.entity.TeamInvitation;

@Getter
@Builder
public class InvitationResponse {
    private String invitationId;
    private String teamId;
    private String teamName;
    private String event;
    private String region;

    public static InvitationResponse from(TeamInvitation invitation) {
        return InvitationResponse.builder()
                .invitationId(invitation.getInvitationId())
                .teamId(invitation.getTeam().getTeamId())
                .teamName(invitation.getTeam().getTeamName())
                .event(invitation.getTeam().getEvent().name())
                .region(invitation.getTeam().getRegion())
                .build();
    }
}
