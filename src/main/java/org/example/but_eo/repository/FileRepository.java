package org.example.but_eo.repository;

import org.example.but_eo.entity.File;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    void deleteAllByUserHashId(Users user);
}

