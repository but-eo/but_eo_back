package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.BoardRequest;
import org.example.but_eo.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성 (파일 포함)
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBoard(@RequestPart BoardRequest request,
                                         @RequestPart(required = false) List<MultipartFile> files,
                                         @RequestParam String userId) {

        boardService.createBoard(request, files, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("게시글이 등록되었습니다.");
    }
}

