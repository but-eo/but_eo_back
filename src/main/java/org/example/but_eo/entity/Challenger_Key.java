package org.example.but_eo.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Challenger_Key implements Serializable {
    private String match_id;
    private String team_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Challenger_Key that = (Challenger_Key) o;
        return match_id.equals(that.match_id) && team_id.equals(that.team_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match_id, team_id);
    }
}
