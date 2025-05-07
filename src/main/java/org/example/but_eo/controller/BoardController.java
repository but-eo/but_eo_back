package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.BoardDetailResponse;
import org.example.but_eo.dto.BoardRequest;
import org.example.but_eo.dto.BoardResponse;
import org.example.but_eo.entity.Board;
import org.example.but_eo.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //게시판 간단 조회
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards(
            @RequestParam Board.Event event,
            @RequestParam Board.Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<BoardResponse> boardList = boardService.getBoardsByEventAndCategory(event, category, page, size);
        return ResponseEntity.ok(boardList);
    }


    //게시판 상세조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(@PathVariable String boardId) {
        BoardDetailResponse detail = boardService.getBoardDetail(boardId);
        return ResponseEntity.ok(detail);
    }

    // 게시글 수정
    @PatchMapping(value = "/{boardId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBoard(@PathVariable String boardId,
                                         @RequestPart BoardRequest request,
                                         @RequestPart(required = false) List<MultipartFile> files) {
        String userId = SecurityUtil.getCurrentUserId(); // JWT에서 유저 ID 추출
        boardService.updateBoard(boardId, request, files, userId);
        return ResponseEntity.ok("게시글 수정 완료");
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable String boardId) {
        String userId = SecurityUtil.getCurrentUserId(); // JWT에서 유저 ID 추출
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }

    //게시글 완전 삭제
    @DeleteMapping("/{boardId}/hard")
    public ResponseEntity<?> deleteBoardHard(@PathVariable String boardId) {
        String userId = SecurityUtil.getCurrentUserId();
        boardService.deleteBoardHard(boardId, userId);
        return ResponseEntity.ok("게시글 완전 삭제 완료");
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

