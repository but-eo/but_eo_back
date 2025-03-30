package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardMapping {

    @EmbeddedId
    private BoardMappingKey boardMappingKey;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @MapsId("fileId")
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
