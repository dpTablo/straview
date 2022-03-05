package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.StravaSyncInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StravaSyncInfoRepository extends JpaRepository<StravaSyncInfo, Long> {
}
