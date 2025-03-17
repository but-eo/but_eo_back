package org.example.but_eo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Board {

    @Id
    private String board_id;

    private enum status{
        PUBLIC, PRIVATE, DELETE
    } //공개, 비공개, 삭제

    private String title; //게시글 제목

    private String content; //게시글 내용

    private String like_count;

    private String comment_count;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    //TODO 유저 연결, 카테고리 분리, 사진 개수 설정
}
