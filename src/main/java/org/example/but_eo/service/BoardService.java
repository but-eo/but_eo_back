package org.example.but_eo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.BoardDetailResponse;
import org.example.but_eo.dto.BoardRequest;
import org.example.but_eo.dto.BoardResponse;
import org.example.but_eo.dto.CommentResponse;
import org.example.but_eo.entity.Board;
import org.example.but_eo.entity.BoardMapping;
import org.example.but_eo.entity.Comment;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.BoardMappingRepository;
import org.example.but_eo.repository.BoardRepository;
import org.example.but_eo.repository.CommentRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UsersRepository usersRepository;
    private final BoardMappingRepository boardMappingRepository;
    private final CommentRepository commentRepository;
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

    //간단조회
    public List<BoardResponse> getBoardsByCategory(Board.Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Board> boards = boardRepository.findByCategoryAndState(category, Board.State.PUBLIC, pageable);

        return boards.stream().map(board -> new BoardResponse(
                board.getBoardId(),
                board.getTitle(),
                board.getUser().getName(),
                board.getCategory(),
                board.getCommentCount(),
                board.getLikeCount(),
                board.getCreatedAt()
        )).toList();
    }

    //상세조회
    public BoardDetailResponse getBoardDetail(String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        List<BoardMapping> mappings = boardMappingRepository.findByBoard_BoardId(boardId);
        List<String> fileUrls = mappings.stream()
                .map(mapping -> mapping.getFile().getFilePath())
                .collect(Collectors.toList());

        List<Comment> commentList = commentRepository.findByBoard_BoardIdAndStateOrderByCreateAtDesc(
                boardId, Comment.State.PUBLIC
        );

        List<CommentResponse> commentResponses = commentList.stream()
                .map(comment -> new CommentResponse(
                        comment.getCommentId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreateAt(),
                        comment.getLikeCount()
                )).toList();

        return new BoardDetailResponse(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getState(),
                board.getCategory(),
                board.getUser().getName(),
                fileUrls,
                board.getLikeCount(),
                board.getCommentCount(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                commentResponses
        );
    }


    // 게시글 수정
    public void updateBoard(String boardId, BoardRequest request, List<MultipartFile> files, String userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (!board.getUser().getUserHashId().equals(userId)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setState(request.getState());
        board.setUpdatedAt(LocalDateTime.now());

        boardRepository.save(board);

        if (files != null && !files.isEmpty()) {
            boardMappingRepository.deleteByBoard_BoardId(boardId);
            fileService.uploadAndMapFilesToBoard(files, board);
        }
    }

    // 게시글 삭제
    public void deleteBoard(String boardId, String userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (!board.getUser().getUserHashId().equals(userId)) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        board.setState(Board.State.DELETE);
        boardRepository.save(board);
    }

    //게시글 완전 삭제
    @Transactional
    public void deleteBoardHard(String boardId, String userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (!board.getUser().getUserHashId().equals(userId)) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        // 1. 댓글 삭제
        commentRepository.deleteAllByBoard_BoardId(boardId);

        // 2. 파일 매핑 삭제
        boardMappingRepository.deleteByBoard_BoardId(boardId);

        // 3. 게시글 삭제
        boardRepository.delete(board);
    }


}

