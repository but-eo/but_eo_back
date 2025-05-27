package org.example.but_eo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingDTO {
    private String roomId;
    private String roomName;
    private List<UserDto> users;

    private String lastMessage; // 마지막 메시지
    private LocalDateTime lastMessageTime; // 마지막 메시지 시간
}
