package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.StadiumRequest;
import org.example.but_eo.dto.StadiumResponse;
import org.example.but_eo.service.StadiumService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stadiums")
public class StadiumController {

    private final StadiumService stadiumService;

    // 생성 - form-data 방식 (이미지 포함)
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createStadium(
            @RequestParam("stadiumName") String stadiumName,
            @RequestParam("stadiumRegion") String stadiumRegion,
            @RequestParam("stadiumMany") int stadiumMany,
            @RequestParam("availableDays") String availableDays,
            @RequestParam("availableHours") String availableHours,
            @RequestParam("stadiumTel") String stadiumTel,
            @RequestParam("stadiumCost") int stadiumCost,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();

        StadiumRequest request = new StadiumRequest();
        request.setStadiumName(stadiumName);
        request.setStadiumRegion(stadiumRegion);
        request.setStadiumMany(stadiumMany);
        request.setAvailableDays(availableDays);
        request.setAvailableHours(availableHours);
        request.setStadiumTel(stadiumTel);
        request.setStadiumCost(stadiumCost);
        request.setImageFiles(images);

        stadiumService.createStadium(request, userId);
        return ResponseEntity.ok("경기장 등록 완료");
    }

    // 수정 (form-data 방식)
    @PatchMapping(value = "/{stadiumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateStadium(
            @PathVariable String stadiumId,
            @ModelAttribute StadiumRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();

        System.out.println("컨트롤러 - 받은 이미지 수: " + (images == null ? "null" : images.size()));

        req.setImageFiles(images); // 이미지 DTO에 주입
        stadiumService.updateStadium(stadiumId, req, userId);
        return ResponseEntity.ok("경기장 수정 완료");
    }


    // 삭제
    @DeleteMapping("/{stadiumId}")
    public ResponseEntity<String> deleteStadium(@PathVariable String stadiumId, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        stadiumService.deleteStadium(stadiumId, userId);
        return ResponseEntity.ok("경기장 삭제 완료");
    }

    // 전체 조회 (StadiumResponse DTO로 반환)
    @GetMapping
    public ResponseEntity<List<StadiumResponse>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.getAll());
    }

    // 단건 조회
    @GetMapping("/{stadiumId}")
    public ResponseEntity<StadiumResponse> getStadium(@PathVariable String stadiumId) {
        return ResponseEntity.ok(stadiumService.getStadiumById(stadiumId));
    }
}
