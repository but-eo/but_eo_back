package org.example.but_eo.repository;

import org.example.but_eo.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, String> {

    // 종목, 상태 기반 조회 (선택적으로 지역이나 날짜도 필터링 가능)
    List<Matching> findByMatchTypeAndState(Matching.Match_Type matchType, Matching.State state);

    // 상태 기반 최신순 조회
    List<Matching> findByStateOrderByMatchDateDesc(Matching.State state);

    // 팀 ID로 매치 목록
    List<Matching> findByTeam_TeamId(String teamId);

    //경기장 기준으로 지역가져옴
    Page<Matching> findByMatchTypeAndStadium_StadiumRegionAndState(Matching.Match_Type matchType, String region, Matching.State state, Pageable pageable);
    Page<Matching> findByMatchTypeAndState(Matching.Match_Type matchType, Matching.State state, Pageable pageable);

    Page<Matching> findByRegionAndState(String region, Matching.State state, Pageable pageable);

    Page<Matching> findByState(Matching.State state, Pageable pageable);

}

