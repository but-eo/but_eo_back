package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Column(length = 64, nullable = false)
    private String user_hash_id; //해시256

    public enum State{
        ACTIVE, DELETED_WAIT
    }; //상태 -> 활성화, 삭제대기

    public enum Division{
        USER, ADMIN, BUSINESS
    }; //유저, 관리자, 사업자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Division division;

    @Column(length = 20, nullable = false)
    private String name; //닉네임

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String tel;

    @Column(length = 30, nullable = true)
    private String prefer_sports; //선호종목

    @Column(length = 30, nullable = false)
    private String region; //지역

    @Column(nullable = true)
    private int badminton_score;

    @Column(nullable = true)
    private int tennis_score;

    @Column(nullable = true)
    private int table_tennis_score;

    @Column(nullable = true)
    private int bowling_Score;

    @Column(nullable = false)
    private boolean gender; //성별 0 : 남자, 1: 여자

    @Column(nullable = true)
    private LocalDate birth;

    @Column(nullable = true)
    private String profile; //프로필 사진

    @Column(nullable = false)
    private LocalDateTime created_at; //계정 생성일

    @OneToMany(mappedBy = "user")
    private List<Board> board_List = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comment_List = new ArrayList<>();

    @OneToMany(mappedBy = "sender_user")
    private List<Notification> sender_List = new ArrayList<>();

    @OneToMany(mappedBy = "receiver_user")
    private List<Notification> receiver_List = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Chatting_Member> chating_List = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private  List<Team_Member> team_Member_List = new ArrayList<>();
}
