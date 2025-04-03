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

    public static TeamInvitation create(Team team, Users user) {
        return TeamInvitation.builder()
                .team(team)
                .user(user)
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
