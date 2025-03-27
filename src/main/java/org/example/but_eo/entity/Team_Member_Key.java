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
public class Team_Member_Key {
    private String user_hash_id;
    private String team_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team_Member_Key that = (Team_Member_Key) o;
        return user_hash_id.equals(that.user_hash_id) && team_id.equals(that.team_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_hash_id, team_id);
    }
}
