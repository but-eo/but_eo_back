package org.example.but_eo.repository;

import org.example.but_eo.entity.TeamMember;
import org.example.but_eo.entity.TeamMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberKey> {
}
