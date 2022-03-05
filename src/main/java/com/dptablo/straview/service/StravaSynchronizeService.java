package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.StravaSyncInfo;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.repository.StravaSyncInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StravaSynchronizeService {
    private final StravaSyncInfoRepository syncInfoRepository;
    private final StravaAthleteService athleteService;
    private final ApplicationProperty applicationProperty;

    @Transactional
    public boolean synchronize() throws StraviewException {
        Long athleteId = applicationProperty.getStravaClientAthleteId();

        StravaSyncInfo syncInfo = syncInfoRepository.findById(athleteId)
                .orElse(StravaSyncInfo.builder()
                        .athleteId(athleteId)
                        .build());
        syncInfo.setSyncEpochTime(Instant.now().getEpochSecond());
        syncInfoRepository.save(syncInfo);

        //TODO athlete 동기화
        StravaAthlete athlete = athleteService.getLoggedInAthlete();

        //TODO gear 동기화

        //TODO activity 동기화

        return false;
    }
}
