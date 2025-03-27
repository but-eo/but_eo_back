package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chatting_Member {

    @EmbeddedId
    private Chatting_Member_Key chatting_Member_Key;

    @ManyToOne
    @MapsId("user_hash_id")
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users user;

    @ManyToOne
    @MapsId("chat_id")
    @JoinColumn(name = "chat_id", nullable = false)
    private Chatting chatting;
}
