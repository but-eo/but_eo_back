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
public class Chatting_Message_Mapping {

    @EmbeddedId
    private Chatting_Message_Mapping_Key chatting__Message_Mapping_key;

    @ManyToOne
    @MapsId("message_id")
    @JoinColumn(name = "message_id", nullable = false)
    private Chatting_Message chatting_Message;

    @ManyToOne
    @MapsId("file_id")
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
