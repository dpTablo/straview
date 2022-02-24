package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.StravaAthlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StravaAthleteRepository  extends JpaRepository<StravaAthlete, Long> {
}
