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
public class Stadium_Mapping_Key {
    private String stadium_id;
    private String file_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stadium_Mapping_Key that = (Stadium_Mapping_Key) o;
        return stadium_id.equals(that.stadium_id) && file_id.equals(that.file_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stadium_id, file_id);
    }
}
