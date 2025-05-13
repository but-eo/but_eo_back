package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequestDto {
    private String email;
    private String name; // 닉네임
    private String password;
    private String tel;
    //private String verificationCode;
    private String gender;
    private String preferSports;
    private String birthYear;
    private String region;

    private String division; // USER 또는 BUSINESS
    private String businessNumber; // BUSINESS인 경우 필수
}
