package org.example.but_eo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Team {

    @Id
    private String team_id;

    private String team_name;

    private String region;

    private String team_avg; // 팀 평균 연령

    private String rating; // 팀 점수

    private enum Team_case{
        TEENAGER, UNIVERSITY, OFFICE, CLUB, FEMALE, ETC
    } //청소년, 대학생, 직장인, 동호회, 여성, 기타

    private Integer total_members;

    private String team_img; //팀 프로필 사진

    private String team_description; //팀 설명

    private Integer total_review; //팀 점수

    @ManyToMany(mappedBy = "teams")
    private Set<Users> users = new HashSet<>();
}
