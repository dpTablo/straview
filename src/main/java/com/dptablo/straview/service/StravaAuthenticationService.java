package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
import com.dptablo.straview.repository.StravaOAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class StravaAuthenticationService {
    private final StravaOAuthRepository stravaOAuthRepository;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>
     *     Strava API 에 신규 토큰 교환을 요청합니다. 이전에 authorize 단계에서 획득한 매개변수 값이 필요합니다.<br>
     *     자세한 내용은 아래 링크의 문서에 있습니다.<br>
     * </p>
     *
     * @see <a href="https://developers.strava.com/docs/authentication">Strava Authentication</a>
     * </p>
     *
     * @param code The code parameter obtained in the redirect.
     * @param grantType The grant type for the request. For initial authentication, must always be "authorization_code".
     * @return 신규 토큰 정보
     */
    public StravaOAuthTokenInfo newAuthenticate(
            String code,
            String grantType
    ) throws AuthenticationException {
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        URI uri = uriBuilderFactory.uriString(applicationProperty.getStravaApiOAuth2Token())
                .queryParam("client_id", applicationProperty.getStravaClientId())
                .queryParam("client_secret", applicationProperty.getClientSecret())
                .queryParam("code", code)
                .queryParam("grant_type", grantType)
                .build();

        try {
            return requestStravaTokenApi(uriBuilderFactory, uri);
        } catch (Exception e) {
            throw new AuthenticationException(
                    StraviewErrorCode.STRAVA_TOKEN_EXCHANGE_FAILED,
                    StraviewErrorCode.STRAVA_TOKEN_EXCHANGE_FAILED.getDescription());
        }
    }

    /**
     * <p>
     *     Strava API 에 기존 토큰 갱신을 요청합니다.<br>
     *     자세한 내용은 아래 링크의 문서에 있습니다.<br>
     * </p>
     *
     * @see <a href="https://developers.strava.com/docs/authentication">Strava Authentication</a>
     * </p>
     * @param grantType The grant type for the request. When refreshing an access token, must always be "refresh_token".
     * @return 갱신 토큰 정보
     */
    public StravaOAuthTokenInfo refreshAuthenticate(String grantType) throws AuthenticationException {
        StravaOAuthTokenInfo tokenInfo = stravaOAuthRepository.findById(applicationProperty.getStravaClientAthleteId())
                .orElseThrow(() -> new AuthenticationException(
                        StraviewErrorCode.STRAVA_AUTHENTICATION_TOKEN_NOT_FOUND,
                        "Refresh token not found.")
                );

        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        URI uri = uriBuilderFactory.uriString(applicationProperty.getStravaApiOAuth2Token())
                .queryParam("client_id", applicationProperty.getStravaClientId())
                .queryParam("client_secret", applicationProperty.getClientSecret())
                .queryParam("grant_type", grantType)
                .queryParam("refresh_token", tokenInfo.getRefreshToken())
                .build();

        try {
            return requestStravaTokenApi(uriBuilderFactory, uri);
        } catch (Exception e) {
            throw new AuthenticationException(
                    StraviewErrorCode.STRAVA_TOKEN_REFRESH_FAILED,
                    StraviewErrorCode.STRAVA_TOKEN_REFRESH_FAILED.getDescription());
        }
    }

    private StravaOAuthTokenInfo requestStravaTokenApi(DefaultUriBuilderFactory uriBuilderFactory, URI uri) {
        Mono<StravaOAuthTokenInfo> tokenInfoMono = WebClient.builder().uriBuilderFactory(uriBuilderFactory).build()
                .post()
                .uri(uri)
                .retrieve()
                .bodyToMono(StravaOAuthTokenInfo.class);

        return tokenInfoMono.blockOptional().orElseThrow(NullPointerException::new);
    }


}