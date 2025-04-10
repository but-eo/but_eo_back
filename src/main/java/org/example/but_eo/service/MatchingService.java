package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.MatchCreateRequest;
import org.example.but_eo.entity.Matching;
import org.example.but_eo.entity.Stadium;
import org.example.but_eo.entity.Team;
import org.example.but_eo.entity.TeamMember;
import org.example.but_eo.repository.*;
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

        matchingRepository.save(matching);
    }


}

