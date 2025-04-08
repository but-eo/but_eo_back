package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.BoardRequest;
import org.example.but_eo.entity.Board;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.BoardRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UsersRepository usersRepository;
    private final FileService fileService; // 업로드 및 BoardMapping 처리용

    public void createBoard(BoardRequest request, List<MultipartFile> files, String userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Board board = new Board();
        board.setBoardId(UUID.randomUUID().toString());
        board.setUser(user);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setCategory(request.getCategory());
        board.setState(request.getState());
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(null);
        board.setCommentCount(0);
        board.setLikeCount(0);

        boardRepository.save(board);

        // 파일 업로드 및 매핑
        if (files != null && !files.isEmpty()) {
            fileService.uploadAndMapFilesToBoard(files, board);
        }
    }
}

