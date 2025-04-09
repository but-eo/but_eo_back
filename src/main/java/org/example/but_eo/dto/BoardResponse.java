package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.but_eo.entity.Board;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponse {
    private String boardId;
    private String title;
    private String userName;
    private Board.Category category;
    private int commentCount;
    private int likeCount;
    private LocalDateTime createdAt;
}
