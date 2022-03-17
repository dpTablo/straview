package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.dptablo.straview.repository.StravaAthleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class StravaAthleteService {
    private final StravaWebClientFactory stravaWebClientFactory;
    private final StravaAthleteRepository stravaAthleteRepository;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>strava에 인증된 사용자의 선수정보를 요청합니다.</p>
     * <p>요청받은 선수정보는 DB에 반영됩니다.</p>
     *
     * @return 요청에 성공한 선수정보
     * @throws AuthenticationException
     */
    @Transactional
    public StravaAthlete getLoggedInAthlete() throws AuthenticationException {
        WebClient webClient = stravaWebClientFactory.createApiWebClient();
        Mono<ResponseEntity<StravaAthlete>> responseEntityMono = webClient.get()
                .uri(applicationProperty.getStravaApiV3UrlAthlete())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(StravaAthlete.class);

        try {
            ResponseEntity<StravaAthlete> stravaAthleteResponseEntity =
                    responseEntityMono.blockOptional(Duration.ofSeconds(applicationProperty.getStravaApiV3Timeout()))
                            .orElseThrow(NullPointerException::new);

            StravaAthlete latestAthlete = stravaAthleteResponseEntity.getBody();
            return stravaAthleteRepository.save(latestAthlete);
        } catch (Throwable t) {
            throw new AuthenticationException(StraviewErrorCode.STRAVA_API_REQUEST_FAILED, t);
        }
    }
}
