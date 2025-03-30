package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChallengerList {

    @EmbeddedId
    private ChallengerKey challengerKey;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id", nullable = false)
    private Matching matching;
}
