package org.example.but_eo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Stadium {

    @Id
    private String stadium_id;

    @Column(length = 30, nullable = false)
    private String stadium_name;

    @Column(length = 30, nullable = false)
    private String stadium_region; //경기장 위치

    private String stadium_many; //수용 인원

    @Column(length = 30, nullable = false)
    private String availableDays; //이용 가능일 ex)월,화,수,목, 금

    @Column(length = 30, nullable = false)
    private String availableHours; //이용 가능일 ex)09:00~23:00

    @Column(length = 30)
    private String stadium_tel;

    private Integer stadium_cost; // 대여 금액

}
