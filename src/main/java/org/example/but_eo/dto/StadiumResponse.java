package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StadiumResponse {

    private String stadiumId;
    private String stadiumName;
    private String stadiumRegion;
    private int stadiumMany;
    private String availableDays;
    private String availableHours;
    private String stadiumTel;
    private int stadiumCost;

    private String ownerNickname; // optional

    private List<String> imageUrls;
}
