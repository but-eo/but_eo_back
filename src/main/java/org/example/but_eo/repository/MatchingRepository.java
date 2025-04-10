package org.example.but_eo.repository;

import org.example.but_eo.entity.Matching;
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
}

