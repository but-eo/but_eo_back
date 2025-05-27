package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String invitationId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }

    private LocalDateTime createdAt;

    public enum Direction {
        INVITE, REQUEST
    }

    @Enumerated(EnumType.STRING)
    private Direction direction;

    public static TeamInvitation createInvite(Team team, Users user) {
        return TeamInvitation.builder()
                .team(team)
                .user(user)
                .status(Status.PENDING)
                .direction(Direction.INVITE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static TeamInvitation createRequest(Team team, Users user) {
        return TeamInvitation.builder()
                .team(team)
                .user(user)
                .status(Status.PENDING)
                .direction(Direction.REQUEST)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
