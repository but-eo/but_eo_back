package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ChallengerTeamResponse;
import org.example.but_eo.dto.MatchCreateRequest;
import org.example.but_eo.dto.MatchingDetailResponse;
import org.example.but_eo.dto.MatchingListResponse;
import org.example.but_eo.entity.*;
import org.example.but_eo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final StadiumRepository stadiumRepository;
    private final MatchingRepository matchingRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ChallengerListRepository challengerListRepository;

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

    @Transactional
    public void applyChallenge(String matchId, String userId) {
        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매치 없음"));

        if (matching.getState() != Matching.State.WAITING) {
            throw new RuntimeException("이미 도전이 수락된 매치입니다.");
        }

        // 현재 유저가 속한 팀 찾기 (도전자)
        TeamMember member = teamMemberRepository.findByUser_UserHashId(userId)
                .orElseThrow(() -> new RuntimeException("팀 멤버가 아님"));
        Team challengerTeam = member.getTeam();

        // 자기가 만든 매치에 도전 x
        if (matching.getTeam().getTeamId().equals(challengerTeam.getTeamId())) {
            throw new RuntimeException("자기 팀 매치에는 도전할 수 없습니다.");
        }

        // 중복 도전 x
        ChallengerKey key = new ChallengerKey(matchId, challengerTeam.getTeamId());
        if (challengerListRepository.existsById(key)) {
            throw new RuntimeException("이미 도전 신청한 매치입니다.");
        }

        // 도전 신청 저장
        ChallengerList challenger = new ChallengerList();
        challenger.setChallengerKey(key);
        challenger.setMatching(matching);
        challenger.setTeam(challengerTeam);
        challengerListRepository.save(challenger);
    }

    public List<ChallengerTeamResponse> getChallengerTeams(String matchId, String userId) {
        Matching match = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매치가 존재하지 않습니다."));

        Team hostTeam = match.getTeam();

        // 요청자가 리더인지 확인
        boolean isLeader = hostTeam.getTeamMemberList().stream()
                .anyMatch(m -> m.getUser().getUserHashId().equals(userId)
                        && m.getType() == TeamMember.Type.LEADER);
        if (!isLeader) {
            throw new RuntimeException("리더만 도전 목록을 조회할 수 있습니다.");
        }

        // 도전자 목록 가져오기
        List<ChallengerList> challengers = challengerListRepository.findByMatching_MatchId(matchId);

        return challengers.stream()
                .map(c -> {
                    Team team = c.getTeam();
                    return new ChallengerTeamResponse(
                            team.getTeamId(),
                            team.getTeamName(),
                            team.getRegion(),
                            team.getRating()
                    );
                })
                .toList();
    }



}

