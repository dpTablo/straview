package com.dptablo.straview.reactive;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.service.StravaOAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StravaWebClientFactoryTest {
    @InjectMocks
    private StravaWebClientFactory factory;

    @Mock
    private ApplicationProperty applicationProperty;

    @Mock
    private StravaOAuthService stravaOAuthService;

    @Test
    public void createApiWebClient() throws AuthenticationException {
        //given
        given(applicationProperty.getStravaApiV3UrlBase()).willReturn("https://www.strava.com/api/v3");

        long athleteId = 12345L;
        long expiresIn = 21600L;

        Instant instant = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
        long nowEpochSecond = zonedDateTime.toEpochSecond();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athleteId)
                .tokenType("Bearer")
                .accessToken("aa")
                .expiresAt(nowEpochSecond + expiresIn)
                .expiresIn(expiresIn)
                .refreshToken("aa11aa11")
                .build();

        given(stravaOAuthService.authenticate()).willReturn(tokenInfo);

        //when
        WebClient webClient = factory.createApiWebClient();

        //then
        assertThat(webClient).isNotNull();
    }

    @Test
    public void createApiWebClient_AuthenticationException() throws AuthenticationException {
        //given
        given(applicationProperty.getStravaApiV3UrlBase()).willReturn("https://www.strava.com/api/v3");
        given(stravaOAuthService.authenticate()).willThrow(AuthenticationException.class);

        //when & then
        assertThatThrownBy(() -> factory.createApiWebClient()).isInstanceOf(AuthenticationException.class);
    }
}