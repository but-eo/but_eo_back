package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchCreateRequest {
    private String teamName;
    private String matchType;
    private String matchDay;  // yyyy-MM-dd
    private String matchTime; // HH:mm
    private String loan;      // "true" or "false"
    private String region;
    private String etc;
}
