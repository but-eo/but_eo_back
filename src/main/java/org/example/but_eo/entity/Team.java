package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team {

    @Id
    @Column(length = 64, nullable = false)
    private String teamId;

    public enum Team_Type {
        SOLO, TEAM
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team_Type teamType;

    @Column(nullable = true)
    private String teamImg; //팀 프로필 사진

    @Column(length = 20, nullable = false)
    private String teamName;

    @Column(length = 30, nullable = false)
    private String region;

    @Column(nullable = false)
    private int memberAge; // 팀 평균 연령

    @Column(nullable = false)
    private int rating; // 팀 점수

    public enum Team_Case{
        TEENAGER, UNIVERSITY, OFFICE, CLUB, FEMALE, ETC
    } //청소년, 대학생, 직장인, 동호회, 여성, 기타

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Team_Case teamCase;

    @Column(nullable = false)
    private int totalMembers;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String teamDescription; //팀 설명

    @Column(nullable = true)
    private int totalReview; //리뷰 총합점

    public enum Event{
        SOCCER, FUTSAL, BASEBALL, BASKETBALL, BADMINTON, TENNIS, TABLE_TENNIS, BOWLING
    } //축구, 풋살, 야구, 농구, 배드민턴, 테니스, 탁구, 볼링

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event event;

    @Column(nullable = false)
    private int matchCount;

    @Column(nullable = false)
    private int winCount;

    @Column(nullable = false)
    private int loseCount;

    @Column(nullable = false)
    private int drawCount;

    @OneToMany(mappedBy = "team")
    private List<ChallengerList> challengerList = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Matching> matchingList = new ArrayList<>();

    @OneToMany(mappedBy = "challengerTeam")
    private List<Matching> challengerTeamList = new ArrayList<>();

    @OneToMany(mappedBy = "winnerTeam")
    private List<Matching> winnerTeamList = new ArrayList<>();

    @OneToMany(mappedBy = "loserTeam")
    private List<Matching> loserTeamList = new ArrayList<>();
}
