package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StravaOAuthRepository extends JpaRepository<StravaOAuthTokenInfo, Long> {
}
