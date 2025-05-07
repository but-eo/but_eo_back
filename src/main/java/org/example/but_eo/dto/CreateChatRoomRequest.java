package org.example.but_eo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateChatRoomRequest {
    private List<String> userHashId;
    private String chatRoomName;
}
