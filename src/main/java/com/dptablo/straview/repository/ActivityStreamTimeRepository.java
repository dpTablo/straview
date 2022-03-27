package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStreamTime;
import com.dptablo.straview.dto.entity.SummaryActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityStreamTimeRepository extends JpaRepository<ActivityStreamTime, Long> {
    Optional<ActivityStreamTime> findBySummaryActivity(SummaryActivity activity);
}
