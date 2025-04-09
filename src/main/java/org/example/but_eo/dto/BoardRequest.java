package org.example.but_eo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.but_eo.entity.Board;

@Getter
@Setter
public class BoardRequest {
    private String title;
    private String content;
    private Board.State state;
    private Board.Category category;
}
