package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.common.OptionalConsumer;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.SummaryActivity;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StravaApiException;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.dptablo.straview.repository.GearRepository;
import com.dptablo.straview.repository.StravaAthleteRepository;
import com.dptablo.straview.repository.SummaryActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StravaSummaryActivityService {
    private final StravaWebClientFactory stravaWebClientFactory;
    private final SummaryActivityRepository summaryActivityRepository;
    private final StravaAthleteRepository athleteRepository;
    private final GearRepository gearRepository;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>Strava에 선수 액티비티에 대한 데이터를 요청합니다.</p>
     * <p>afterEpoch 시간 값을 기준으로 이후의 모든 액티비티에 대하여 perPage 만큼 요청합니다.</p>
     *
     * @param afterEpoch 해당 시간 이후 데이터를 대해 요청. epoch
     * @param perPage 요청당 조회할 액티비티의 수
     * @return 조회 요청에 성공한 모든 액티비티 리스트
     * @throws StraviewException Strava API 요청 오류
     */
    public List<SummaryActivity> getLoggedInAthleteActivities(long afterEpoch, int perPage)
            throws StraviewException
    {
        try {
            WebClient webClient = stravaWebClientFactory.createApiWebClient();

            StravaAthlete athlete = athleteRepository.findById(applicationProperty.getStravaClientAthleteId())
                    .orElseThrow(NullPointerException::new);

            ArrayList<SummaryActivity> resultList = new ArrayList<>();
            List<SummaryActivity> activities;
            while(!(activities = requestGetLoggedInAthleteActivities(webClient, athlete, afterEpoch, perPage))
                    .isEmpty()
            ) {
                resultList.addAll(activities);

                SummaryActivity lastActivity = activities.get(activities.size() - 1);
                afterEpoch = getStartDateEpoch(lastActivity);
            }
            return resultList;
        } catch(AuthenticationException e) {
            throw e;
        } catch (Throwable t) {
            throw new StravaApiException(StraviewErrorCode.STRAVA_API_REQUEST_FAILED, t.getMessage());
        }
    }

    private List<SummaryActivity> requestGetLoggedInAthleteActivities(WebClient webClient, StravaAthlete athlete, long afterEpoch, int perPage) {
        Mono<List<SummaryActivity>> summaryActivitiesMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(applicationProperty.getStravaApiV3UrlAthleteActivities())
                        .queryParam("after", afterEpoch)
                        .queryParam("page", 1)
                        .queryParam("per_page", perPage)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SummaryActivity>>() {});

        List<SummaryActivity> summaryActivities =
                summaryActivitiesMono.blockOptional(
                        Duration.ofSeconds(applicationProperty.getStravaApiV3Timeout())
                ).orElseThrow(NullPointerException::new);

        for(SummaryActivity summaryActivity : summaryActivities) {
            OptionalConsumer.of(
                    gearRepository.findByGearIdAndAthleteId(
                            summaryActivity.getGearId(), summaryActivity.getAthlete().getAthleteId())
            ).ifPresent(summaryActivity::setGear);

            summaryActivity.setAthlete(athlete);
        }
        return summaryActivities;
    }

    private long getStartDateEpoch(SummaryActivity lastActivity) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
        LocalDateTime startDate = LocalDateTime.parse(lastActivity.getStartDate(), dateTimeFormatter);
        return startDate.toEpochSecond(ZoneOffset.UTC);
    }
}
