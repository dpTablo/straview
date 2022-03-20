package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.Gear;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StravaApiException;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.dptablo.straview.repository.GearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StravaGearService {
    private final StravaWebClientFactory stravaWebClientFactory;
    private final GearRepository gearRepository;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>strava에 특정 선수의 기어정보를 요청합니다.</p>
     * <p>요청받은 기어정보는 DB에 반영됩니다.</p>
     *
     * @param athlete 기어소유 선수
     * @param gearIdList 요청할 기어 id
     * @return 처리 완료된 Gear
     * @throws AuthenticationException Strava 인증 오류
     * @throws StravaApiException Strava API 요청 오류
     */
    @Transactional
    public List<Gear> getGearById(StravaAthlete athlete, List<String> gearIdList)
            throws AuthenticationException, StravaApiException
    {
        WebClient webClient = stravaWebClientFactory.createApiWebClient();

        List<Gear> gearList = new ArrayList<>();
        for(String gearId : gearIdList) {
            Mono<Gear> gearMono = webClient.get()
                    .uri(applicationProperty.getStravaApiV3UrlGear() + "/" + gearId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Gear.class);

            Gear gear = gearMono.blockOptional()
                    .orElseThrow(() -> new StravaApiException(
                            StraviewErrorCode.STRAVA_API_REQUEST_FAILED,
                            StraviewErrorCode.STRAVA_API_REQUEST_FAILED.getDescription()
                    ));

            gear.setAthlete(athlete);
            gearList.add(gear);
        }
        return gearRepository.saveAll(gearList);
    }
}
