package org.example.but_eo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String matchId;
    private String targetTeamId;
    private int rating;
    private String content;
}

