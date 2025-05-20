package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StadiumRequest {
    private String stadiumName;
    private String stadiumRegion;
    private int stadiumMany;
    private String availableDays;
    private String availableHours;
    private String stadiumTel;
    private int stadiumCost;
}
