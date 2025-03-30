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
public class TeamMember {

    @EmbeddedId
    private TeamMemberKey teamMemberKey;

    @ManyToOne
    @MapsId("userHashId")
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users user;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public enum Type {
        LEADER, MEMBER
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(length = 20, nullable = false)
    private String position;
}
