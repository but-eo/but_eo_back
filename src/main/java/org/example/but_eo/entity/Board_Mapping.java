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
public class Board_Mapping {

    @EmbeddedId
    private Board_Mapping_Key board_mapping_key;

    @ManyToOne
    @MapsId("board_id")
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @MapsId("file_id")
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
