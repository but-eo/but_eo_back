package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, EXIT
    };

    private MessageType type;
    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime createdAt;

}
