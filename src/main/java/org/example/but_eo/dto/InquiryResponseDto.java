package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.InquiryAnswer;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class InquiryResponseDto {
    private String inquiryId;
    private String title;
    private String content;
    private String writerName;
    private Inquiry.Visibility visibility;
    private LocalDateTime createdAt;

    private String answerContent;
    private String adminName;
    private LocalDateTime answeredAt;

    public static InquiryResponseDto from(Inquiry inquiry) {
        InquiryAnswer answer = inquiry.getAnswer();

        return InquiryResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .writerName(inquiry.getUser().getName())
                .visibility(inquiry.getVisibility())
                .createdAt(inquiry.getCreatedAt())
                .answerContent(answer != null ? answer.getContent() : null)
                .adminName(answer != null ? answer.getAdmin().getName() : null)
                .answeredAt(answer != null ? answer.getAnsweredAt() : null)
                .build();
    }
}