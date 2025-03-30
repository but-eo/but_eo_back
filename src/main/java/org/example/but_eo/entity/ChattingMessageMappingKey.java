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
public class ChattingMessageMappingKey {
    private String messageId;
    private String fileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChattingMessageMappingKey that = (ChattingMessageMappingKey) o;
        return messageId.equals(that.messageId) && fileId.equals(that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, fileId);
    }
}
