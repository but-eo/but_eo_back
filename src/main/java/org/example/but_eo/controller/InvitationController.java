package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.InvitationResponse;
import org.example.but_eo.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    //초대 수락
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<String> acceptInvitation(
            @PathVariable String invitationId,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        invitationService.acceptInvitation(invitationId, userId);
        return ResponseEntity.ok("초대 수락 완료");
    }

    //초대 거절
    @PostMapping("/{invitationId}/decline")
    public ResponseEntity<String> declineInvitation(
            @PathVariable String invitationId,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        invitationService.declineInvitation(invitationId, userId);
        return ResponseEntity.ok("초대 거절 완료");
    }

    @GetMapping("/me")
    public ResponseEntity<List<InvitationResponse>> getMyInvitations(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        List<InvitationResponse> invitations = invitationService.getPendingInvitations(userId);
        return ResponseEntity.ok(invitations);
    }

}
