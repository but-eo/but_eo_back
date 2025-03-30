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
@Setter
@Getter
public class TeamMemberKey {
    private String userHashId;
    private String teamId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamMemberKey that = (TeamMemberKey) o;
        return userHashId.equals(that.userHashId) && teamId.equals(that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userHashId, teamId);
    }
}
