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
public class Chatting_Message_Mapping_Key {
    private String message_id;
    private String file_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chatting_Message_Mapping_Key that = (Chatting_Message_Mapping_Key) o;
        return message_id.equals(that.message_id) && file_id.equals(that.file_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_id, file_id);
    }
}
