package com.dptablo.straview.repository;

import com.dptablo.straview.dto.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
