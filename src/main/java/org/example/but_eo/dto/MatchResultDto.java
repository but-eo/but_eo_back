package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.but_eo.entity.Matching;

@Data
@AllArgsConstructor
public class MatchResultDto {
    private String matchId;
    private Matching.State status;
    private String opponentTeamName;
}
