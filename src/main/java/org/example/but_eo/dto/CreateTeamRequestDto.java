package org.example.but_eo.dto;

import lombok.Data;

@Data
public class CreateTeamRequestDto {
    private String teamName;
    private String event;
    private String region;
    private int memberAge;
    private String teamCase;
}
