package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

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
    private String board_id;

    @ManyToOne
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users user;

    public enum State{
        PUBLIC, PRIVATE, DELETE
    } //공개, 비공개, 삭제

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(length = 50, nullable = false)
    private String title; //게시글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; //게시글 내용

    @Column(nullable = false)
    private int like_count;

    @Column(nullable = false)
    private int comment_count;

    @Column(nullable = true)
    private LocalDateTime updated_at;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment_List = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Board_Mapping> board_Mapping_List = new ArrayList<>();

    //TODO 유저 연결, 카테고리 분리, 사진 개수 설정
}
