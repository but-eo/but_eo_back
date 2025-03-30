package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Column(length = 64, nullable = false)
    private String userHashId; //해시256

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
    private String preferSports; //선호종목

    @Column(length = 30, nullable = false)
    private String region; //지역

    @Column(nullable = true)
    private int badmintonScore;

    @Column(nullable = true)
    private int tennisScore;

    @Column(nullable = true)
    private int tableTennisScore;

    @Column(nullable = true)
    private int bowlingScore;

    @Column(nullable = false)
    private String gender; //성별 0 : 남자, 1: 여자

    @Column(nullable = true)
    private String birth;

    @Column(nullable = true)
    private String profile; //프로필 사진

    @Column(nullable = false)
    private LocalDateTime createdAt; //계정 생성일

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "senderUser")
    private List<Notification> senderList = new ArrayList<>();

    @OneToMany(mappedBy = "receiverUser")
    private List<Notification> receiverList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChattingMember> chatingList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private  List<TeamMember> teamMemberList = new ArrayList<>();
}
