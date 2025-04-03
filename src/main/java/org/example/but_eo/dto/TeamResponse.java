package org.example.but_eo.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.but_eo.entity.Team;

@Getter
@Builder
public class TeamResponse {
    private String teamId;
    private String teamName;
    private String region;
    private int memberAge;
    private int rating;
    private String teamDescription;
    private String teamImg;
    private String event;
    private String teamType;
    private String teamCase;

    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .region(team.getRegion())
                .memberAge(team.getMemberAge())
                .rating(team.getRating())
                .teamDescription(team.getTeamDescription())
                .teamImg(team.getTeamImg())
                .event(team.getEvent().name())
                .teamType(team.getTeamType().name())
                .teamCase(team.getTeamCase() != null ? team.getTeamCase().name() : null)
                .build();
    }
}
