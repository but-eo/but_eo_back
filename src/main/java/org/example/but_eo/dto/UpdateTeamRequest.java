package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateTeamRequest {
    private String teamName;
    private String region;
    private Integer memberAge;
    private String teamCase;
    private String teamDescription;
    private MultipartFile teamImg;
    private String teamType;
}

