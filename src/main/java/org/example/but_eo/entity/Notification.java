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
public class Notification {

    @Id
    private String alarm_id;

    private String alarm_content;

    private enum Type{
        MATCH_INVITED, MATCH_RESULT, TEAM_REQUEST, TEAM_ACCEPTED, TEAM_DECLINED, GENERAL, SYSTEM
    } //있으면 좋을 것 같아서, 매치 초대, 매치 결과, 팀 가입 요청, 팀 가입 승인, 팀 가입 거부, 일반, 시스템

    private enum Status {
        UNREAD, READ, DELETED
    } //안읽음, 읽음, 삭제됨

    private LocalDateTime notification_date; //알람 전송일

    @ManyToOne
    private Users user; //유저 외래키
}
