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
public class MatchingListResponse {
    private String matchId;
    private String matchRegion;
    private String teamName;
    private String teamImg;
    private String teamRegion;
    private int teamRating;
    private String stadiumName;
    private String stadiumRegion;
    private LocalDateTime matchDate;
    private String matchType;
    private Boolean loan;
}

