package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String userName;
    private String division; // USER, BUSINESS
}
