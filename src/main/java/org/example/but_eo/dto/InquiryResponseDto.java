package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.InquiryAnswer;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryResponseDto {
    private String inquiryId;
    private String title;
    private String content;
    private String userName;
    private Inquiry.Visibility visibility;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    public static InquiryResponseDto from(Inquiry inquiry) {
        InquiryResponseDto dto = new InquiryResponseDto();
        dto.setInquiryId(inquiry.getInquiryId());
        dto.setTitle(inquiry.getTitle());
        dto.setContent(inquiry.getContent());
        dto.setUserName(inquiry.getUser().getName());
        dto.setVisibility(inquiry.getVisibility());
        dto.setCreatedAt(inquiry.getCreatedAt());

        InquiryAnswer answer = inquiry.getAnswer();
        if (answer != null) {
            dto.setAnswer(answer.getContent());
            dto.setAnsweredAt(answer.getAnsweredAt());
        }
        return dto;
    }
}
