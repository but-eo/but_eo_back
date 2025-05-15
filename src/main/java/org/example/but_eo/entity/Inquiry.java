package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @Column(length = 64)
    private String inquiryId;

    @ManyToOne
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users user;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 100, nullable = true)
    private String password; // 비공개용 비밀번호 (없으면 누구나 열람 가능)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility; // 공개 여부

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "inquiry", cascade = CascadeType.ALL)
    private InquiryAnswer answer;

    public enum Visibility {
        PUBLIC, PRIVATE
    }
}