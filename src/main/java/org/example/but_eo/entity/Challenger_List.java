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
public class Challenger_List {

    @EmbeddedId
    private Challenger_Key challenger_key;

    @ManyToOne
    @MapsId("team_id")
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @MapsId("match_id")
    @JoinColumn(name = "match_id", nullable = false)
    private Matching matching;
}
