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
public class ChattingMessageMapping {

    @EmbeddedId
    private ChattingMessageMappingKey chattingMessageMappingkey;

    @ManyToOne
    @MapsId("messageId")
    @JoinColumn(name = "message_id", nullable = false)
    private ChattingMessage chattingMessage;

    @ManyToOne
    @MapsId("fileId")
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
