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
public class MatchCreateRequest {
    private String stadiumId;
    private LocalDateTime matchDate;
    private Boolean loan;               //대여 여부
    private String etc;                 //기타
}

