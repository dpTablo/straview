package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStreamTime;
import com.dptablo.straview.dto.entity.ActivityStreamWatts;
import com.dptablo.straview.dto.entity.SummaryActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityStreamWattsRepository extends JpaRepository<ActivityStreamWatts, Long> {
    Optional<ActivityStreamWatts> findBySummaryActivity(SummaryActivity activity);
}
