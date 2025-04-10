package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.internal.build.AllowNonPortable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllowNonPortable
@Entity
@Getter
@Setter
public class Matching {

    @Id
    @Column(length = 64, nullable = false)
    private String matchId;

    @ManyToOne
    @JoinColumn(name = "stadium_id", nullable = true)
    private Stadium stadium;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "Challenger", nullable = true)
    private Team challengerTeam;

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = true)
    private Team winnerTeam;

    @ManyToOne
    @JoinColumn(name = "loser_id", nullable = true)
    private Team loserTeam;

    public enum State {
        SUCCESS ,COMPLETE, CANCEL, WAITING
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Column(length = 30, nullable = true)
    private String region;


    public enum Match_Type {
        SOCCER, FUTSAL, BASEBALL, BASKETBALL, BADMINTON, TENNIS, TABLE_TENNIS, BOWLING
    } //축구, 풋살, 야구, 농구, 배드민턴, 테니스, 탁구, 볼링

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Match_Type matchType;

    @Column(nullable = false)
    private Boolean loan;

    @Column(nullable = true)
    private int winnerScore;

    @Column(nullable = true)
    private int loserScore;

    @Column(nullable = true)
    private String etc;

    @OneToMany(mappedBy = "matching")
    private List<ChallengerList> challengerList = new ArrayList<>();
}
