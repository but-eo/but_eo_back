package org.example.but_eo.entity;

import jakarta.persistence.Column;
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
public class ChattingMemberKey implements Serializable {
    @Column(name = "user_hash_id")
    private String userHashId;
    @Column(name = "chat_id")
    private String chatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChattingMemberKey that = (ChattingMemberKey) o;
        return userHashId.equals(that.userHashId) && chatId.equals(that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userHashId, chatId);
    }
}
