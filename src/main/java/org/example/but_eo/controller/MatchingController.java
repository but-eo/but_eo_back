package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.MatchCreateRequest;
import org.example.but_eo.dto.MatchingDetailResponse;
import org.example.but_eo.dto.MatchingListResponse;
import org.example.but_eo.entity.Matching;
import org.example.but_eo.service.MatchingService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<MatchingListResponse>> getMatchings(
            @RequestParam(required = false) Matching.Match_Type matchType,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MatchingListResponse> result = matchingService.getMatchings(matchType, region, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchingDetailResponse> getMatchDetail(@PathVariable String matchId) {
        MatchingDetailResponse response = matchingService.getMatchDetail(matchId);
        return ResponseEntity.ok(response);
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

