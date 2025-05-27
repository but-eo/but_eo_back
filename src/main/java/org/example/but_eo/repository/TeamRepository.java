package org.example.but_eo.repository;

import org.example.but_eo.entity.Team;
import org.example.but_eo.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {
}
