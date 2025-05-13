package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.InquiryCreateRequestDto;
import org.example.but_eo.dto.InquiryResponseDto;
import org.example.but_eo.dto.InquiryAnswerRequestDto;
import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.service.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/create")
    public ResponseEntity<?> createInquiry(@RequestBody InquiryCreateRequestDto dto, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        inquiryService.createInquiry(userId, dto.getTitle(), dto.getContent(), dto.getPassword(), dto.getVisibility());
        return ResponseEntity.ok("문의 등록 성공");
    }

    @GetMapping("/public")
    public ResponseEntity<List<InquiryResponseDto>> getPublicInquiries(Authentication auth) {
        String userId = (auth != null) ? (String) auth.getPrincipal() : null;
        List<Inquiry> list = inquiryService.getPublicAndOwnInquiries(userId);
        return ResponseEntity.ok(list.stream().map(InquiryResponseDto::from).toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<InquiryResponseDto>> getMyInquiries(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        List<InquiryResponseDto> list = inquiryService.getMyInquiries(userId).stream()
                .map(InquiryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> getInquiryDetail(@PathVariable String inquiryId,
                                                               @RequestParam(required = false) String password,
                                                               Authentication auth) {
        String userId = (auth != null) ? (String) auth.getPrincipal() : null;
        Inquiry inquiry = inquiryService.getInquiryDetail(inquiryId, userId, password);
        return ResponseEntity.ok(InquiryResponseDto.from(inquiry));
    }

    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<?> answerInquiry(@PathVariable String inquiryId,
                                           @RequestBody InquiryAnswerRequestDto dto,
                                           Authentication auth) {
        String adminId = (String) auth.getPrincipal();
        inquiryService.answerInquiry(inquiryId, adminId, dto.getContent());
        return ResponseEntity.ok("답변 등록 완료");
    }
}

