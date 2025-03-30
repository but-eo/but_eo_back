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
public class ChallengerKey implements Serializable {
    private String matchId;
    private String teamId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengerKey that = (ChallengerKey) o;
        return matchId.equals(that.matchId) && teamId.equals(that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, teamId);
    }
}
