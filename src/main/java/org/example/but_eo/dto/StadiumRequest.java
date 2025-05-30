package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class StadiumRequest {
    private String stadiumName;
    private String stadiumRegion;
    private Integer stadiumMany;
    private String availableDays;
    private String availableHours;
    private String stadiumTel;
    private Integer stadiumCost;

    private List<MultipartFile> imageFiles; // 최대 10장
}
