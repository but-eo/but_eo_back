package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.TeamInviteRequest;
import org.example.but_eo.dto.TeamResponse;
import org.example.but_eo.dto.UpdateTeamRequest;
import org.example.but_eo.entity.Team;
import org.example.but_eo.service.TeamService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    //팀 생성
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createTeam(
            @RequestParam("team_name") String teamName,
            @RequestParam("event") String eventStr,
            @RequestParam("region") String region,
            @RequestParam("member_age") int memberAge,
            @RequestParam("team_case") String teamCaseStr,
            @RequestParam("team_description") String teamDescription,
            @RequestPart(value = "team_img", required = false) MultipartFile teamImg,
            Authentication authentication) {

        Team.Event event = Team.Event.valueOf(eventStr);
        Team.Team_Case teamCase = Team.Team_Case.valueOf(teamCaseStr);
        String userId = (String) authentication.getPrincipal();

        teamService.createTeam(teamName, event, region, memberAge, teamCase, teamDescription, teamImg, userId);
        return ResponseEntity.ok("팀 생성 성공");
    }

    //팀 수정
    @PatchMapping(value = "/{teamId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTeam(
            @PathVariable String teamId,
            @ModelAttribute UpdateTeamRequest request,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        teamService.updateTeam(teamId, request, userId);
        System.out.println("팀 업데이트 성공");
        return ResponseEntity.ok("팀 정보 수정 완료");
    }

    //팀 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable String teamId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        teamService.deleteTeam(teamId, userId);
        System.out.println("팀 삭제 성공");
        return ResponseEntity.ok("팀 삭제 완료");
    }

    //팀 전체 조회(아무 조건 없을때), 팀 검색 조건(조건 있으면 필터링)
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeamsWithFilter(
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String teamType,
            @RequestParam(required = false) String teamCase,
            @RequestParam(required = false) String teamName) {

        List<TeamResponse> teams = teamService.getFilteredTeams(event, region, teamType, teamCase, teamName);
        return ResponseEntity.ok(teams);
    }

    //초대
    @PostMapping("/{teamId}/invite")
    public ResponseEntity<String> inviteUserToTeam(
            @PathVariable String teamId,
            @RequestBody TeamInviteRequest request,
            Authentication authentication) {

        String leaderId = (String) authentication.getPrincipal();
        teamService.inviteUserToTeam(teamId, request.getUserId(), leaderId);
        return ResponseEntity.ok("초대 성공");
    }

    // 초대 취소
    @DeleteMapping("/{teamId}/invitecancel/{userId}")
    public ResponseEntity<String> cancelInvitation(
            @PathVariable String teamId,
            @PathVariable String userId,
            Authentication auth) {

        String leaderId = (String) auth.getPrincipal();
        teamService.cancelInvitation(teamId, userId, leaderId);
        return ResponseEntity.ok("초대를 취소하였습니다.");
    }

    // 팀 탈퇴
    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<String> leaveTeam(@PathVariable String teamId, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        teamService.leaveTeam(teamId, userId);
        return ResponseEntity.ok("팀에서 탈퇴하였습니다.");
    }

    // 팀원 방출
    @DeleteMapping("/{teamId}/kick/{userId}")
    public ResponseEntity<String> kickMember(
            @PathVariable String teamId,
            @PathVariable String userId,
            Authentication auth) {

        String leaderId = (String) auth.getPrincipal();
        teamService.kickMember(teamId, userId, leaderId);
        return ResponseEntity.ok("해당 유저를 팀에서 방출하였습니다.");
    }

    //유저 롤 체크
    @GetMapping("/{teamId}/role")
    public ResponseEntity<String> getTeamRole(
            @PathVariable String teamId,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        String role = teamService.getTeamRole(teamId, userId);

        return ResponseEntity.ok(role);
    }


}
