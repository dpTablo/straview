package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.common.OptionalConsumer;
import com.dptablo.straview.dto.entity.*;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StravaSynchronizeService {
    public static final int PER_PAGE = 100;

    private final StravaSummaryActivityService stravaSummaryActivityService;
    private final StravaAthleteService athleteService;
    private final StravaActivityStreamService stravaActivityStreamService;

    private final StravaSyncInfoRepository syncInfoRepository;
    private final StravaAthleteRepository athleteRepository;
    private final GearRepository gearRepository;
    private final SummaryActivityRepository summaryActivityRepository;
    private final ActivityStreamRepository activityStreamRepository;

    private final ApplicationProperty applicationProperty;

    /**
     * <p>Strava 의 데이터를 요청하여 Straview DB에 동기화 합니다.</p>
     *
     * @return 동기화 정보
     * @throws StraviewException 동기화 프로세스 과정의 에러
     */
    @Transactional(rollbackFor = StraviewException.class)
    public StravaSyncInfo synchronize() throws StraviewException, JsonProcessingException {
        StravaSyncInfo syncInfo = getSyncInfo();

        StravaAthlete athlete = processingAthleteAndGears();
        List<SummaryActivity> savedActivities = processingActivities(syncInfo, athlete);
        processingActivityStreams(savedActivities);
        return processingSyncInfo(syncInfo);
    }

    private List<SummaryActivity> processingActivities(StravaSyncInfo syncInfo, StravaAthlete athlete) throws StraviewException {
        List<SummaryActivity> activities = stravaSummaryActivityService.getLoggedInAthleteActivities(syncInfo.getSyncEpochTime(), PER_PAGE);
        activities.forEach(activity -> activity.setFtp(athlete.getFtp()));
        return summaryActivityRepository.saveAll(activities);
    }

    private void processingActivityStreams(List<SummaryActivity> activities) throws AuthenticationException, JsonProcessingException {
        List<ActivityStreamType> types = Arrays.asList(
                ActivityStreamType.ALTITUDE, ActivityStreamType.GRADE_SMOOTH, ActivityStreamType.VELOCITY_SMOOTH,
                ActivityStreamType.TEMP, ActivityStreamType.TIME, ActivityStreamType.LATLNG,
                ActivityStreamType.CADENCE, ActivityStreamType.HEARTRATE, ActivityStreamType.WATTS,
                ActivityStreamType.MOVING, ActivityStreamType.DISTANCE);

        for(SummaryActivity activity : activities) {
            List<ActivityStream> activityStreams = stravaActivityStreamService.getActivityStreams(activity, types);
            activityStreamRepository.saveAll(activityStreams);
        }
    }

    private StravaSyncInfo getSyncInfo() {
        Long athleteId = applicationProperty.getStravaClientAthleteId();
        return syncInfoRepository.findById(athleteId)
                .orElse(StravaSyncInfo.builder()
                        .athleteId(athleteId)
                        .syncEpochTime(0L)
                        .build());
    }

    private StravaSyncInfo processingSyncInfo(StravaSyncInfo syncInfo) {
        syncInfo.setSyncEpochTime(Instant.now().getEpochSecond());
        return syncInfoRepository.save(syncInfo);
    }

    private StravaAthlete processingAthleteAndGears() throws AuthenticationException {
        StravaAthlete athlete = athleteService.getLoggedInAthlete();

        //athlete 정보의 bike 에 대한 기존 데이터 pk 매핑
        List<Gear> bikes = athlete.getBikes();
        for(Gear bike : bikes) {
            OptionalConsumer.of(
                    gearRepository.findByGearIdAndAthleteId(bike.getGearId(), bike.getAthlete().getAthleteId())
            ).ifPresent(foundGear -> bike.setManageId(foundGear.getManageId()));
        }

        return athleteRepository.save(athlete);
    }
}
