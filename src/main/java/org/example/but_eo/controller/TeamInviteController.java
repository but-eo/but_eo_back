package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.TeamInviteRequest;
import org.example.but_eo.service.TeamInviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams/{teamId}/invite")
public class TeamInviteController {

    private final TeamInviteService teamInviteService;

    // 초대 전송 (POST /api/teams/{teamId}/invite)
    @PostMapping
    public ResponseEntity<String> invite(@PathVariable String teamId, @RequestBody TeamInviteRequest request, Authentication auth) {
        String leaderId = (String) auth.getPrincipal();
        teamInviteService.inviteUserToTeam(teamId, request, leaderId);
        return ResponseEntity.ok("초대 완료");
    }

    // 초대 취소 (DELETE /api/teams/{teamId}/invite/{userId})
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> cancel(@PathVariable String teamId, @PathVariable String userId, Authentication auth) {
        String leaderId = (String) auth.getPrincipal();
        teamInviteService.cancelInvite(teamId, userId, leaderId);
        return ResponseEntity.ok("초대 취소 완료");
    }
}
