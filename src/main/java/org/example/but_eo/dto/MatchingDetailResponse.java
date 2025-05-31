package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.but_eo.entity.Matching;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingDetailResponse {
    private String matchId;
    private String teamName;
    private String teamRegion;
    private String stadiumName;
    private String stadiumRegion;
    private LocalDateTime matchDate;
    private Boolean loan;
    private String matchType;
    private String etc;
    private String challengerTeamName;
    private Integer winnerScore;
    private Integer loserScore;
    private Matching.State state;
}
