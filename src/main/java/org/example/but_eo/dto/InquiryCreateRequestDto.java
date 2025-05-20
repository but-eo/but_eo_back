package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryCreateRequestDto {
    private String title;
    private String content;
    private String password; // null이면 공개로 간주
}
