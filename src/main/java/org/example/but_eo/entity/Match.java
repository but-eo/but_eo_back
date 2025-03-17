package org.example.but_eo.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Match {

    @Id
    private String match_Id;

    @Column(length = 50)
    private String match_Name;

    @Column(length = 50)
    private String match_Region;

    @Column(length = 100)
    private String match_Date; //시작~끝으로 잡을지 한 문장으로 받을지

    @Column(length = 50)
    private String match_location;

    @Column(length = 50, nullable = true) //널값 허용
    private String match_host; //주최

    @Column(length = 50, nullable = true) //널값 허용
    private String match_organize; //주관
}
