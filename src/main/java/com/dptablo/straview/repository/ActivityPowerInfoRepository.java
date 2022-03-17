package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityPowerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPowerInfoRepository extends JpaRepository<ActivityPowerInfo, Long> {
}
