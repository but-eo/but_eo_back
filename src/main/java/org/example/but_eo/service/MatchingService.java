package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.MatchCreateRequest;
import org.example.but_eo.dto.MatchingDetailResponse;
import org.example.but_eo.dto.MatchingListResponse;
import org.example.but_eo.entity.Matching;
import org.example.but_eo.entity.Stadium;
import org.example.but_eo.entity.Team;
import org.example.but_eo.entity.TeamMember;
import org.example.but_eo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final StadiumRepository stadiumRepository;
    private final MatchingRepository matchingRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public void createMatch(MatchCreateRequest request, String userId) {
        // 리더로 있는 팀 찾기
        TeamMember leader = teamMemberRepository.findByUser_UserHashIdAndType(userId, TeamMember.Type.LEADER)
                .orElseThrow(() -> new RuntimeException("리더 팀이 없습니다."));

        Team team = leader.getTeam();

        Stadium stadium = null;
//        if (request.getStadiumId() != null) {
//            stadium = stadiumRepository.findById(request.getStadiumId())
//                    .orElseThrow(() -> new RuntimeException("경기장 없음"));
//        }

        Matching matching = new Matching();
        matching.setMatchId(UUID.randomUUID().toString());
        matching.setTeam(team);
        matching.setStadium(stadium);
        matching.setMatchDate(request.getMatchDate());
        matching.setMatchType(Matching.Match_Type.valueOf(team.getEvent().name())); //팀 종목 그대로 가져오기
        matching.setLoan(request.getLoan());
        matching.setEtc(request.getEtc());
        matching.setState(Matching.State.WAITING);
        matching.setRegion(team.getRegion());
        matchingRepository.save(matching);
    }

    public Page<MatchingListResponse> getMatchings(Matching.Match_Type matchType, String region, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("matchDate").descending());

        Page<Matching> matchingPage;

        if (matchType != null && region != null) {
            matchingPage = matchingRepository.findByMatchTypeAndStadium_StadiumRegionAndState(
                    matchType, region, Matching.State.WAITING, pageable);
        } else if (matchType != null) {
            matchingPage = matchingRepository.findByMatchTypeAndState(
                    matchType, Matching.State.WAITING, pageable);
        } else if (region != null) {
            matchingPage = matchingRepository.findByRegionAndState(
                    region, Matching.State.WAITING, pageable);
        } else {
            matchingPage = matchingRepository.findByState(Matching.State.WAITING, pageable);
        }

        return matchingPage.map(m -> new MatchingListResponse(
                m.getMatchId(),
                m.getTeam().getTeamName(),
                m.getTeam().getRegion(),
                m.getStadium() != null ? m.getStadium().getStadiumName() : "미정",
                m.getMatchDate(),
                m.getMatchType(),
                m.getLoan()
        ));
    }

    public MatchingDetailResponse getMatchDetail(String matchId) {
        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매치가 존재하지 않습니다."));

        return new MatchingDetailResponse(
                matching.getMatchId(),
                matching.getTeam().getTeamName(),
                matching.getTeam().getRegion(),
                matching.getStadium() != null ? matching.getStadium().getStadiumName() : "미정",
                matching.getStadium() != null ? matching.getStadium().getStadiumRegion() : "미정",
                matching.getMatchDate(),
                matching.getLoan(),
                matching.getMatchType(),
                matching.getEtc(),
                matching.getChallengerTeam() != null ? matching.getChallengerTeam().getTeamName() : null,
                matching.getWinnerScore(),
                matching.getLoserScore(),
                matching.getState()
        );
    }




}

