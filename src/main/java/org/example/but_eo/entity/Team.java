package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team {

    @Id
    @Column(length = 64, nullable = false)
    private String team_id;

    public enum Team_Type {
        SOLO, TEAM
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team_Type team_type;

    @Column(nullable = true)
    private String team_img; //팀 프로필 사진

    @Column(length = 20, nullable = false)
    private String team_name;

    @Column(length = 30, nullable = false)
    private String region;

    @Column(nullable = false)
    private int member_age; // 팀 평균 연령

    @Column(nullable = false)
    private int rating; // 팀 점수

    public enum Team_Case{
        TEENAGER, UNIVERSITY, OFFICE, CLUB, FEMALE, ETC
    } //청소년, 대학생, 직장인, 동호회, 여성, 기타

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Team_Case team_case;

    @Column(nullable = false)
    private int total_members;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String team_description; //팀 설명

    @Column(nullable = true)
    private int total_review; //리뷰 총합점

    public enum Event{
        SOCCER, FUTSAL, BASEBALL, BASKETBALL, BADMINTON, TENNIS, TABLE_TENNIS, BOWLING
    } //축구, 풋살, 야구, 농구, 배드민턴, 테니스, 탁구, 볼링

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event event;

    @Column(nullable = false)
    private int match_count;

    @Column(nullable = false)
    private int win_count;

    @Column(nullable = false)
    private int lose_count;

    @Column(nullable = false)
    private int draw_count;

    @OneToMany(mappedBy = "team")
    private List<Challenger_List> challenger_List = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Team_Member> team_member_list = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Matching> matching_List = new ArrayList<>();

    @OneToMany(mappedBy = "Challenger_Team")
    private List<Matching> Challenger_Team_List = new ArrayList<>();

    @OneToMany(mappedBy = "winner_Team")
    private List<Matching> winner_Team_List = new ArrayList<>();

    @OneToMany(mappedBy = "loser_Team")
    private List<Matching> loser_Team_List = new ArrayList<>();
}
