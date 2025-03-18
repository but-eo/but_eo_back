package org.example.but_eo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chatting_Message {

    @Id
    private String message_id;

    private String message;

    private enum Read{
        YES, NO
    } //읽음, 안읽음

    private LocalDateTime created_at;


    //TODO : 채팅방과 연결
    @ManyToOne
    private Users user; //유저 식별 번호(유저 아이디)

    @ManyToOne
    private Chatting chatting_room; //채팅방 식별 번호(채팅방 아이디)

}
