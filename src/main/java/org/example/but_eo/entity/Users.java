package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    private String id; //해시256

    private enum State{
        ACTIVE, DELETED_WAIT
    }; //상태 -> 활성화, 삭제대기

    @Column(length = 20, nullable = false)
    private String name; //닉네임


    @Column(length = 80, unique = true, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String tel;

    @Column(length = 30, nullable = true)
    private String prefer_sports; //선호종목

    @Column(length = 30, nullable = false)
    private String region; //지역

    @Column(nullable = true)
    private LocalDate birth;

    @Column(nullable = true)
    private String profile; //프로필 사진

    @Column(nullable = false)
    private LocalDateTime created_at; //계정 생성일

    @Column(nullable = false)
    private boolean gender; //성별 0 : 남자, 1: 여자

    private enum Division{
        USER, ADMIN, BUSINESS
    }; //유저, 관리자, 사업자

    @ManyToMany
    @JoinTable(
            name = "chatting_user", //매핑 테이블 이름
            joinColumns = @JoinColumn(name = "user_id"), //유저 fk
            inverseJoinColumns = @JoinColumn(name = "chatting_id") //채팅 fk
    )
    private Set<Chatting> chattings = new HashSet<>();;

    @ManyToMany
    @JoinTable(
            name = "team_member",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams = new HashSet<>();;
}
