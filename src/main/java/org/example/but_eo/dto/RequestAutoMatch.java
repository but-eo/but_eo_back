package org.example.but_eo.dto;

import lombok.Data;

@Data
public class RequestAutoMatch {
    private String teamId;
    private String userId;
    private String sportType;
    private String region;
}
