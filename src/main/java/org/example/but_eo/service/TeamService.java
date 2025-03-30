package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final TeamMemberRepository teamMemberRepository;

    public void createTeam(String teamName, Team.Event event, String region,
                           int memberAge, Team.Team_Case teamCase,
                           MultipartFile teamImg, String userId) {

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
