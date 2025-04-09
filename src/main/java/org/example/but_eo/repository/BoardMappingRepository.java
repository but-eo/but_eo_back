package org.example.but_eo.repository;

import org.example.but_eo.entity.BoardMapping;
import org.example.but_eo.entity.BoardMappingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMappingRepository extends JpaRepository<BoardMapping, BoardMappingKey> {

    // 특정 게시글에 연결된 파일 리스트 조회
    List<BoardMapping> findByBoard_BoardId(String boardId);

    // 특정 게시글의 모든 매핑 삭제 (게시글 삭제 시 함께 제거할 수 있음)
    void deleteByBoard_BoardId(String boardId);
}
