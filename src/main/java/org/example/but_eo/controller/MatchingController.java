package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.MatchCreateRequest;
import org.example.but_eo.service.MatchingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchings")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("/create")
    public ResponseEntity<?> createMatch(@RequestBody MatchCreateRequest request) {
        String userId = SecurityUtil.getCurrentUserId();
        matchingService.createMatch(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("매치 생성 완료");
    }

    public class SecurityUtil {
        public static String getCurrentUserId() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("인증 정보 없음");
            }
            return authentication.getName(); // 또는 JWT claim 기반으로 userId 추출
        }
    }
}

