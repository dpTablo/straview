package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityStreamRepository<T extends ActivityStream> extends JpaRepository<T, Long> {
}