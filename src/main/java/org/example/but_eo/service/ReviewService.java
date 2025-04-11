package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.ReviewRequest;
import org.example.but_eo.entity.*;
import org.example.but_eo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MatchingRepository matchingRepository;
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void writeReview(ReviewRequest request, String userId) {
        // 매치 존재 확인
        Matching match = matchingRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("매치가 존재하지 않습니다."));

        // 매치 상태 확인
        if (match.getState() != Matching.State.COMPLETE) {
            throw new RuntimeException("경기가 완료된 매치만 리뷰 작성이 가능합니다.");
        }

        // 유저의 소속 팀 및 리더 여부 확인
        TeamMember member = teamMemberRepository.findByUser_UserHashId(userId)
                .orElseThrow(() -> new RuntimeException("팀에 소속된 유저가 아닙니다."));

        if (member.getType() != TeamMember.Type.LEADER) {
            throw new RuntimeException("리더만 리뷰를 작성할 수 있습니다.");
        }

        Team writerTeam = member.getTeam();

        // 매치에 참여한 팀인지 확인
        if (!match.getTeam().equals(writerTeam) && !match.getChallengerTeam().equals(writerTeam)) {
            throw new RuntimeException("해당 매치에 참여한 팀만 리뷰 작성이 가능합니다.");
        }

        // 자기 팀을 대상으로 리뷰 작성
        Team targetTeam = teamRepository.findById(request.getTargetTeamId())
                .orElseThrow(() -> new RuntimeException("리뷰 대상 팀이 존재하지 않습니다."));

        if (targetTeam.equals(writerTeam)) {
            throw new RuntimeException("자기 팀에는 리뷰를 남길 수 없습니다.");
        }

        // 중복 리뷰 방지
        boolean alreadyWritten = reviewRepository.existsByMatch_MatchIdAndWriter_UserHashId(request.getMatchId(), userId);
        if (alreadyWritten) {
            throw new RuntimeException("이미 리뷰를 작성하였습니다.");
        }

        // 리뷰 저장
        MatchReview review = new MatchReview();
        //review.setReviewId(UUID.randomUUID().toString());
        review.setMatch(match);
        review.setWriter(usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 없음")));
        review.setTargetTeam(targetTeam);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }
}
