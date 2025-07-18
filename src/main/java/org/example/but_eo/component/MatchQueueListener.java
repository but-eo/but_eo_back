package org.example.but_eo.component;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.RequestAutoMatch;
import org.example.but_eo.entity.Matching;
import org.example.but_eo.entity.Team;
import org.example.but_eo.repository.MatchingRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchQueueListener {

    private final MatchQueue matchQueue;
    private final MatchingRepository matchingRepository;

    @EventListener
    public void handleMatchQueueEvent(MatchQueueEvent event) {
        matchQueue.tryMatch().ifPresent(pair -> {
            RequestAutoMatch reqA = pair.get(0);
            RequestAutoMatch reqB = pair.get(1);

            Matching match = new Matching();
            match.setMatchId(UUID.randomUUID().toString());
            match.setMatchType(Matching.Match_Type.valueOf(reqA.getSportType()));
            match.setMatchRegion(reqA.getRegion());
            match.setState(Matching.State.WAITING);
            match.setMatchDate(LocalDateTime.now());

            //DB에 접근해서 가져오는걸로 변경할 것
            Team teamA = new Team();
            teamA.setTeamId(reqA.getTeamId());

            Team teamB = new Team();
            teamB.setTeamId(reqB.getTeamId());

            match.setTeam(teamA);
            match.setChallengerTeam(teamB);

            matchingRepository.save(match);
        });
    }
}
