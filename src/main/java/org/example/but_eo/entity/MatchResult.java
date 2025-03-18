package org.example.but_eo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchResult {

    @Id
    private String result_id;

    private Integer winner_score;

    private Integer loser_score;

    private LocalDate match_date; // 시합 일정

    //TODO : 팀 테이블과 연결
    @ManyToOne
    private Team winner_id;

    @ManyToOne
    private Team loser_id;

}
