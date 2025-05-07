package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Board {

    @Id
    @Column(length = 64, nullable = false)
    private String boardId;

    @ManyToOne
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users user;

    public enum State{
        PUBLIC, PRIVATE, DELETE
    } //공개, 비공개, 삭제

    public enum Category{
        FREE, REVIEW, TEAM, MEMBER, NOTIFICATION
    }

    public enum Event {
        SOCCER, FUTSAL, BASEBALL, BASKETBALL,
        BADMINTON, TENNIS, TABLE_TENNIS, BOWLING
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event event;

    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(length = 50, nullable = false)
    private String title; //게시글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; //게시글 내용

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int commentCount;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "board")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardMapping> boardMappingList = new ArrayList<>();

    //TODO 유저 연결, 카테고리 분리, 사진 개수 설정
}
