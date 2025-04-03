package org.example.but_eo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.TeamResponse;
import org.example.but_eo.dto.UpdateTeamRequest;
import org.example.but_eo.entity.*;
import org.example.but_eo.repository.TeamMemberRepository;
import org.example.but_eo.repository.TeamRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final Set<Team.Event> soloCompatibleEvents = Set.of(
            Team.Event.BADMINTON,
            Team.Event.TENNIS,
            Team.Event.TABLE_TENNIS,
            Team.Event.BOWLING
    );

    //팀 생성 코드
    public void createTeam(String teamName, Team.Event event, String region,
                           int memberAge, Team.Team_Case teamCase, String teamDescription,
                           MultipartFile teamImg, String userId) {

        // 이미 해당 종목의 팀에 소속되어 있으면 예외 발생
        if (teamMemberRepository.existsByUserAndEvent(userId, event)) {
            throw new IllegalStateException("이미 해당 종목의 팀에 소속되어 있습니다.");
        }

        Users user = usersRepository.findByUserHashId(userId);
        String teamId = UUID.randomUUID().toString();

        String imgUrl = null;
        if (teamImg != null && !teamImg.isEmpty()) {
            imgUrl = saveImage(teamImg);
        }

        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName(teamName);
        team.setEvent(event);
        team.setRegion(region);
        team.setMemberAge(memberAge);
        team.setTeamCase(teamCase);
        team.setTeamDescription(teamDescription);
        team.setTeamImg(imgUrl);
        team.setRating(1000);
        team.setTotalMembers(1);
        team.setMatchCount(0);
        team.setWinCount(0);
        team.setLoseCount(0);
        team.setDrawCount(0);
        team.setTeamType(Team.Team_Type.TEAM);

        teamRepository.save(team);

        TeamMember teamMember = new TeamMember();
        teamMember.setTeamMemberKey(new TeamMemberKey(userId, teamId));
        teamMember.setUser(user);
        teamMember.setTeam(team);
        teamMember.setType(TeamMember.Type.LEADER);
        teamMember.setPosition("주장");

        teamMemberRepository.save(teamMember);
    }

    @Transactional
    public void updateTeam(String teamId, UpdateTeamRequest req, String userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀이 존재하지 않습니다."));

        // 권한 체크: 리더만 수정 가능
        TeamMemberKey key = new TeamMemberKey(userId, teamId);
        TeamMember member = teamMemberRepository.findById(key)
                .orElseThrow(() -> new IllegalStateException("팀에 속해있지 않습니다."));
        if (member.getType() != TeamMember.Type.LEADER) {
            throw new IllegalAccessError("팀 수정은 리더만 할 수 있습니다.");
        }
        
        //팀 타입 (축구 야구 농구 풋살)이거면 솔로 안됨
        if (req.getTeamType() != null) {
            Team.Event currentEvent = team.getEvent();

            if (soloCompatibleEvents.contains(currentEvent)) {
                try {
                    Team.Team_Type newType = Team.Team_Type.valueOf(req.getTeamType());
                    team.setTeamType(newType);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("teamType은 SOLO 또는 TEAM 중 하나여야 합니다.");
                }
            } else {
                throw new IllegalArgumentException("이 종목에서는 teamType을 수정할 수 없습니다.");
            }
        }

        if (req.getTeamName() != null) team.setTeamName(req.getTeamName());
        if (req.getRegion() != null) team.setRegion(req.getRegion());
        if (req.getMemberAge() != null) team.setMemberAge(req.getMemberAge());
        if (req.getTeamCase() != null) team.setTeamCase(Team.Team_Case.valueOf(req.getTeamCase()));
        if (req.getTeamDescription() != null) team.setTeamDescription(req.getTeamDescription());

        if (req.getTeamImg() != null && !req.getTeamImg().isEmpty()) {
            String newImgUrl = saveImage(req.getTeamImg());
            team.setTeamImg(newImgUrl);
        }

        teamRepository.save(team);
        teamRepository.flush();
        System.out.println("teamName: " + team.getTeamName());
        System.out.println("teamDescription: " + team.getTeamDescription());
    }

    @Transactional
    //팀 삭제 코드
    public void deleteTeam(String teamId, String userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀이 존재하지 않습니다."));

        TeamMemberKey key = new TeamMemberKey(userId, teamId);
        TeamMember member = teamMemberRepository.findById(key)
                .orElseThrow(() -> new IllegalStateException("팀에 속해있지 않습니다."));
        if (member.getType() != TeamMember.Type.LEADER) {
            throw new IllegalAccessError("팀 삭제는 리더만 할 수 있습니다.");
        }

        // 팀원 먼저 삭제 → 팀 삭제
        teamMemberRepository.deleteAll(team.getTeamMemberList());
        teamRepository.delete(team);
    }

    //팀 조회
//    public List<TeamResponse> getAllTeams() {
//        List<Team> teams = teamRepository.findAll();
//        return teams.stream()
//                .map(TeamResponse::from)
//                .collect(Collectors.toList());
//    }

    //팀 검색 조건
    public List<TeamResponse> getFilteredTeams(String event, String region, String teamType, String teamCase, String teamName) {
        List<Team> all = teamRepository.findAll();

        return all.stream()
                .filter(team -> event == null || team.getEvent().name().equalsIgnoreCase(event))
                .filter(team -> region == null || team.getRegion().contains(region))
                .filter(team -> teamType == null || team.getTeamType().name().equalsIgnoreCase(teamType))
                .filter(team -> teamCase == null ||
                        (team.getTeamCase() != null && team.getTeamCase().name().equalsIgnoreCase(teamCase)))
                .filter(team -> teamName == null || team.getTeamName().contains(teamName))
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

    //이미지 저장
    private String saveImage(MultipartFile file) {
        try {
            String uploadDir = "src/main/resources/static/images/team";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            return "/images/team/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
