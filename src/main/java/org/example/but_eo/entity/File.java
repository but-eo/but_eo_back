package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

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
    private String file_id; //파일 아이디

    @ManyToOne
    @JoinColumn(name = "user_hash_id", nullable = false)
    private Users user_hash_id;

    @Column(length = 50, nullable = false)
    private String file_name; //파일명

    @Column(length = 100, nullable = false)
    private String file_path; //파일 주소

    @Column(nullable = false)
    private LocalDateTime created_at; //파일 생성일

    @OneToMany(mappedBy = "file")
    private List<Board_Mapping> board_Mapping_List = new ArrayList<>();

    @OneToMany(mappedBy = "file")
    private List<Chatting_Message_Mapping> chatting_Message_Mapping_List = new ArrayList<>();

    @OneToMany(mappedBy = "file")
    private List<Stadium_Mapping> stadium_Mapping_List = new ArrayList<>();
    //TODO: 유저 테이블 연결
}
