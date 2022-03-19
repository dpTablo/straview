package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.Gear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GearRepository extends JpaRepository<Gear, Long> {
    List<Gear> findAllByAthlete_AthleteId(Long athleteId);

    @Query(value = "select g from Gear g where g.gearId = :gearId and g.athlete.athleteId = :athleteId")
    Optional<Gear> findByGearIdAndAthleteId(
            @Param(value = "gearId") String gearId,
            @Param(value = "athleteId") Long athleteId);
}
