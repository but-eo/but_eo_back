package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reviewId;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Matching match;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Users writer;

    @ManyToOne
    @JoinColumn(name = "target_team_id", nullable = false)
    private Team targetTeam;

    @Column(nullable = false)
    private int rating; // 1 ~ 5

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

