package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLoginDto {
    String nickName;
    String profileImage;
    String email;
    String gender;
    String birthYear;
    String refreshToken;
}

