package org.example.but_eo.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Board_Mapping_Key {
    private String board_id;
    private String file_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board_Mapping_Key that = (Board_Mapping_Key) o;
        return board_id.equals(that.board_id) && file_id.equals(that.file_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board_id, file_id);
    }
}
