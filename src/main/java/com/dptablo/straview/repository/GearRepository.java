package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.Gear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GearRepository extends JpaRepository<Gear, Integer> {
    List<Gear> findAllByAthlete_AthleteId(Long athleteId);
}
