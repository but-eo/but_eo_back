package org.example.but_eo.repository;

import org.example.but_eo.entity.StadiumMapping;
import org.example.but_eo.entity.StadiumMappingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StadiumMappingRepository extends JpaRepository<StadiumMapping, StadiumMappingKey> {
    List<StadiumMapping> findAllByStadium_StadiumId(String stadiumId);

}
