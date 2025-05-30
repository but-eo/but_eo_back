package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stadium {

    @Id
    @Column(length = 64, nullable = false)
    private String stadiumId;

    @Column(length = 30, nullable = false)
    private String stadiumName;

    @Column(length = 30, nullable = false)
    private String stadiumRegion; // 경기장 위치

    @Column(nullable = true)
    private int stadiumMany; // 수용 인원

    @Column(length = 30, nullable = false)
    private String availableDays; // 이용 가능일 ex)월,화,수,목, 금

    @Column(length = 30, nullable = false)
    private String availableHours; // 이용 가능시간 ex)09:00~23:00

    @Column(length = 30, nullable = true)
    private String stadiumTel;

    @Column(nullable = true)
    private int stadiumCost; // 대여 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matching> matchingList = new ArrayList<>();

    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumMapping> stadiumMappingList = new ArrayList<>();
}