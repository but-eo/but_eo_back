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
public class Chatting_Member_Key implements Serializable {
    private String user_hash_id;
    private String chat_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chatting_Member_Key that = (Chatting_Member_Key) o;
        return user_hash_id.equals(that.user_hash_id) && chat_id.equals(that.chat_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_hash_id, chat_id);
    }
}
