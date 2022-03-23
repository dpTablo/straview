package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.StravaSyncInfo;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class StravaSynchronizeServiceTest {
    @InjectMocks
    private StravaSynchronizeService service;

    @Mock
    private StravaSummaryActivityService stravaSummaryActivityService;

    @Mock
    private StravaAthleteService athleteService;

    @Mock
    private StravaActivityStreamService stravaActivityStreamService;

    @Mock
    private StravaSyncInfoRepository syncInfoRepository;

    @Mock
    private StravaAthleteRepository athleteRepository;

    @Mock
    private GearRepository gearRepository;

    @Mock
    private SummaryActivityRepository summaryActivityRepository;

    @Mock
    private ActivityStreamRepository activityStreamRepository;

    @Mock
    private ApplicationProperty applicationProperty;

    @Test
    public void synchronize() throws StraviewException, JsonProcessingException {
        //given
        Long athleteId = 123442L;
        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);

        StravaSyncInfo syncInfo = StravaSyncInfo.builder()
                .athleteId(athleteId)
                .syncEpochTime(Instant.now().getEpochSecond())
                .build();
        given(syncInfoRepository.save(syncInfo)).willReturn(syncInfo);

        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(athleteId)
                .build();
        given(athleteService.getLoggedInAthlete()).willReturn(athlete);

        //when
        StravaSyncInfo returnedSyncInfo = service.synchronize();

        //then
        assertThat(returnedSyncInfo).isEqualTo(syncInfo);
    }
}