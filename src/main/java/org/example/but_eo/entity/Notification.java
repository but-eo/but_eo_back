package org.example.but_eo.entity;

import jakarta.persistence.*;
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
public class Notification {

    @Id
    @Column(length = 64, nullable = false)
    private String alarm_id;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private Users sender_user;

    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private Users receiver_user;

    public enum Type{
        MATCH_INVITED, MATCH_RESULT, TEAM_REQUEST, TEAM_ACCEPTED, TEAM_DECLINED, GENERAL, SYSTEM
    } //있으면 좋을 것 같아서, 매치 초대, 매치 결과, 팀 가입 요청, 팀 가입 승인, 팀 가입 거부, 일반, 시스템

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Type type;

    public enum State {
        UNREAD, READ, DELETED
    } //안읽음, 읽음, 삭제됨

    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private LocalDateTime notification_date; //알람 전송일

}
