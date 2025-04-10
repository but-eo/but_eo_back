package org.example.but_eo.repository;

import org.example.but_eo.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, String> {

    // 지역 기반 조회
    List<Stadium> findByStadiumRegion(String stadiumRegion);

    // 이름 포함 검색
    List<Stadium> findByStadiumNameContaining(String keyword);
}

