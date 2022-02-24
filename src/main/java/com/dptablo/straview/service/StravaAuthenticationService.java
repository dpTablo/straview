package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@PropertySource("classpath:app.properties")
@Slf4j
public class StravaAuthenticationService {
    private final RestTemplate restTemplate;

    private final ApplicationProperty applicationProperty;

    public StravaAuthenticationService(RestTemplate restTemplate, ApplicationProperty applicationProperty) {
        this.restTemplate = restTemplate;
        this.applicationProperty = applicationProperty;
    }

    /**
     * <p>
     *     Strava API 에 토큰 교환을 요청합니다. 이전에 authorize 단계에서 획득한 매개변수 값이 필요합니다.<br>
     *     자세한 내용은 아래 링크의 문서에 있습니다.<br>
     * </p>
     *
     * @see <a href="https://developers.strava.com/docs/authentication">Strava Authentication</a>
     * </p>
     *
     * @param code The code parameter obtained in the redirect.
     * @param grantType The grant type for the request. For initial authentication, must always be "authorization_code".
     * @return api 응답 데이터 객체
     */
    public StravaOAuthTokenInfo authenticate(
            String code,
            String grantType
    ) throws AuthenticationException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(applicationProperty.getStravaApiOAuth2Token())
                .queryParam("client_id", applicationProperty.getStravaClientId())
                .queryParam("client_secret", applicationProperty.getClientSecret())
                .queryParam("code", code)
                .queryParam("grant_type", grantType);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        StravaOAuthTokenInfo result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, StravaOAuthTokenInfo.class).getBody();

        if(result == null || result.getAccessToken() == null || result.getAccessToken().isEmpty()) {
            throw new AuthenticationException(StraviewErrorCode.INVALID_STRAVA_CLIENT_SECRET, "Token exchange failed.");
        } else {
            return result;
        }
    }
}
