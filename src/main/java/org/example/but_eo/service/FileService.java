package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.entity.Board;
import org.example.but_eo.entity.BoardMapping;
import org.example.but_eo.entity.BoardMappingKey;
import org.example.but_eo.entity.File;
import org.example.but_eo.repository.BoardMappingRepository;
import org.example.but_eo.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final BoardMappingRepository boardMappingRepository;

    public void uploadAndMapFilesToBoard(List<MultipartFile> files, Board board) {
        for (MultipartFile file : files) {
            String fileId = UUID.randomUUID().toString();
            String filePath = "/uploads/" + fileId + "_" + file.getOriginalFilename();

            // 실제 파일 저장 로직은 생략 or 구현 필요
            // ex: file.transferTo(new File(uploadDir + filePath));

            File savedFile = new File(fileId, board.getUser(), file.getOriginalFilename(), filePath, LocalDateTime.now());
            fileRepository.save(savedFile);

            BoardMapping mapping = new BoardMapping();
            BoardMappingKey key = new BoardMappingKey(board.getBoardId(), fileId);
            mapping.setBoardMappingKey(key);
            mapping.setBoard(board);
            mapping.setFile(savedFile);

            boardMappingRepository.save(mapping);
        }
    }
}

