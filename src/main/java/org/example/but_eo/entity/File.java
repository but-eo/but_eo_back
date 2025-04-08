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
public class File {

    @Id
    @Column(length = 64, nullable = false)
    private String fileId; //파일 아이디

    @ManyToOne
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users userHashId;

    @Column(length = 50, nullable = false)
    private String fileName; //파일명

    @Column(length = 100, nullable = false)
    private String filePath; //파일 주소

    @Column(nullable = false)
    private LocalDateTime createdAt; //파일 생성일

    @OneToMany(mappedBy = "file")
    private List<BoardMapping> boardMappingList = new ArrayList<>();

    @OneToMany(mappedBy = "file")
    private List<ChattingMessageMapping> chattingMessageMappingList = new ArrayList<>();

    @OneToMany(mappedBy = "file")
    private List<StadiumMapping> stadiumMappingList = new ArrayList<>();
    //TODO: 유저 테이블 연결

    public File(String fileId, Users user, String fileName, String filePath, LocalDateTime createdAt) {
        this.fileId = fileId;
        this.userHashId = user;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }
}
