package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStream;
import com.dptablo.straview.dto.entity.ActivityStreamPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityStreamRepository<T extends ActivityStream> extends JpaRepository<T, ActivityStreamPK> {

}
