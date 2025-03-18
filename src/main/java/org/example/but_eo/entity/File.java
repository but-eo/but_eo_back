package org.example.but_eo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class File {

    @Id
    private String file_id; //파일 아이디

    private String file_name; //파일명
    
    private String file_path; //파일 주소

    private LocalDateTime created_at; //파일 생성일
    
    //TODO: 유저 테이블 연결
}
