package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChattingMessage {

    @Id
    @Column(length = 64, nullable = false)
    private String messageId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_hash_id", referencedColumnName = "user_hash_id", nullable = false),
            @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    })
    private ChattingMember chattingMember;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean readCheck;

    public enum Division{
        USER, ADMIN, BUSINESS
    }; //유저, 관리자, 사업자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Division division;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chattingMessage")
    private List<ChattingMessageMapping> chattingMessageMappinglist = new ArrayList<>();
}