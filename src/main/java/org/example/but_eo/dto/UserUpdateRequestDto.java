package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String name;
    private String region;
    private String preferSports;
    private MultipartFile profile; // 이미지 파일
    private String password;
    private String tel;
    private String gender;
}
