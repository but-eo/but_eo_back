package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequestDto {
    private String email;
    private String name; // 닉네임
    private String password;
    private String passwordCheck;
    private String tel;
    private String verificationCode;
    private boolean gender; // true: 여자, false: 남자
    private String preferSports;
    private int birthYear;
    private String region;
}
