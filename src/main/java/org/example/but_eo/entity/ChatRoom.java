package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ChatRoom {

    @Id
    private String chatRoomId;

    private String chatRoomName;

    // Users 엔티티와의 다대다 관계 설정
    @ManyToMany
    @JoinTable(
            name = "chat_room_users", // 조인 테이블의 이름
            joinColumns = @JoinColumn(name = "chat_room_id"), // ChatRoom의 외래 키
            inverseJoinColumns = @JoinColumn(name = "user_hash_id") // Users의 외래 키
    )
    private List<Users> users; // 채팅방에 참여하는 사용자 목록

    public List<String> getUserIds() {
        return users.stream()
                .map(Users::getUserHashId)  // Users 객체에서 userHashId만 추출
                .collect(Collectors.toList());
    }

}
