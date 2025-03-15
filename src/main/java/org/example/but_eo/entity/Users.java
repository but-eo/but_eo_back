package org.example.but_eo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String name; //닉네임

    @Column(unique = true)
    private String email;

    private String password;

    private String tel;

    private String prefer_sports; //선호종목

    private String region; //지역

    private LocalDate birth;

    private String profile; //프로필 사진

    private LocalDateTime created_at; //계정 생성일

    private boolean gender; //성별 0 : 남자, 1: 여자

    private enum Division{
        USER, ADMIN, BUSINESS
    }; //유저, 관리자, 사업자
}
