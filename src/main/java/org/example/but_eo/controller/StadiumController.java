package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.StadiumRequest;
import org.example.but_eo.entity.Stadium;
import org.example.but_eo.service.StadiumService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stadiums")
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<String> createStadium(@RequestBody StadiumRequest request, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        stadiumService.createStadium(request, userId);
        return ResponseEntity.ok("경기장 등록 완료");
    }

    @PatchMapping("/{stadiumId}")
    public ResponseEntity<String> updateStadium(@PathVariable String stadiumId,
                                                @RequestBody StadiumRequest request,
                                                Authentication auth) {
        String userId = (String) auth.getPrincipal();
        stadiumService.updateStadium(stadiumId, request, userId);
        return ResponseEntity.ok("경기장 수정 완료");
    }

    @DeleteMapping("/{stadiumId}")
    public ResponseEntity<String> deleteStadium(@PathVariable String stadiumId,
                                                Authentication auth) {
        String userId = (String) auth.getPrincipal();
        stadiumService.deleteStadium(stadiumId, userId);
        return ResponseEntity.ok("경기장 삭제 완료");
    }

    @GetMapping
    public ResponseEntity<List<Stadium>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.getAll());
    }
}
