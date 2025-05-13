package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.InquiryAnswer;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryAnswerRequestDto {
    private String content;
}
