package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.CommentRequest;
import org.example.but_eo.dto.CommentResponse;
import org.example.but_eo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/{boardId}")
    public ResponseEntity<?> createComment(@PathVariable String boardId,
                                           @RequestBody CommentRequest request) {
        String userId = BoardController.SecurityUtil.getCurrentUserId();
        commentService.createComment(boardId, userId, request);
        return ResponseEntity.ok("댓글 등록 완료");
    }

    //댓글 목록 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String boardId) {
        List<CommentResponse> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable String commentId,
                                           @RequestBody CommentRequest request) {
        String userId = SecurityUtil.getCurrentUserId();
        commentService.updateComment(commentId, userId, request);
        return ResponseEntity.ok("댓글 수정 완료");
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        String userId = SecurityUtil.getCurrentUserId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    //댓글 완전 삭제
    @DeleteMapping("/{commentId}/hard")
    public ResponseEntity<?> deleteCommentHard(@PathVariable String commentId) {
        String userId = SecurityUtil.getCurrentUserId();
        commentService.deleteCommentHard(commentId, userId);
        return ResponseEntity.ok("댓글 완전 삭제 완료");
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

