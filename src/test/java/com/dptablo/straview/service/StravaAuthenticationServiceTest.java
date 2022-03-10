package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.repository.StravaOAuthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StravaAuthenticationServiceTest {
    private static MockWebServer mockWebServer;

    @InjectMocks
    private StravaAuthenticationService service;

    @Mock
    private StravaOAuthRepository stravaOAuthRepository;

    @Mock
    private ApplicationProperty applicationProperty;

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void newAuthenticate() throws AuthenticationException {
        //given
        String stravaApiAuto2Token = String.format("http://localhost:%s/v3/oauth/token", mockWebServer.getPort());

        given(applicationProperty.getStravaApiOAuth2Token()).willReturn(stravaApiAuto2Token);
        given(applicationProperty.getStravaClientId()).willReturn(778899);
        given(applicationProperty.getClientSecret()).willReturn("ssccrreett");


        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .accessToken("a4b945687g...")
                .refreshToken("e5n567567...")
                .expiresAt(1568775134L)
                .expiresIn(21600L)
                .build();
        given(stravaOAuthRepository.save(any(StravaOAuthTokenInfo.class))).willReturn(tokenInfo);

        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    String requestPath = recordedRequest.getPath();
                    if(requestPath.startsWith("/v3/oauth/token") &&
                            requestPath.contains("client_id=") &&
                            requestPath.contains("client_secret=") &&
                            requestPath.contains("code=") &&
                            requestPath.contains("grant_type=")
                    ) {
                        return new MockResponse()
                                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(HttpStatus.OK.value())
                                .setBody(objectMapper.writeValueAsString(tokenInfo));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when
        StravaOAuthTokenInfo resultTokenInfo = service.newAuthenticate("abcd", "authorization_code");

        //then
        assertThat(resultTokenInfo.getTokenType()).isEqualTo(tokenInfo.getTokenType());
        assertThat(resultTokenInfo.getAccessToken()).isEqualTo(tokenInfo.getAccessToken());
        assertThat(resultTokenInfo.getRefreshToken()).isEqualTo(tokenInfo.getRefreshToken());
        assertThat(resultTokenInfo.getExpiresAt()).isEqualTo(tokenInfo.getExpiresAt());
        assertThat(resultTokenInfo.getExpiresIn()).isEqualTo(tokenInfo.getExpiresIn());
    }

    @Test
    public void refreshAuthenticate() throws AuthenticationException {
        //given
        String stravaApiAuto2Token = String.format("http://localhost:%s/v3/oauth/token", mockWebServer.getPort());
        long athleteId = 838485398L;

        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);
        given(applicationProperty.getStravaApiOAuth2Token()).willReturn(stravaApiAuto2Token);
        given(applicationProperty.getStravaClientId()).willReturn(778899);
        given(applicationProperty.getClientSecret()).willReturn("ssccrreett");

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .accessToken("a4b945687g...")
                .refreshToken("e5n567567...")
                .expiresAt(1568775134L)
                .expiresIn(21600L)
                .build();
        given(stravaOAuthRepository.findById(athleteId)).willReturn(Optional.of(tokenInfo));

        StravaOAuthTokenInfo refreshTokenInfo = StravaOAuthTokenInfo.builder()
                .tokenType("Bearer")
                .accessToken("klsdfkln4309kjlre")
                .refreshToken("lkdfsil0943k")
                .expiresAt(1668775134L)
                .expiresIn(20000L)
                .build();

        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String requestPath = recordedRequest.getPath();
                    if(requestPath.startsWith("/v3/oauth/token") &&
                            requestPath.contains("client_id=") &&
                            requestPath.contains("client_secret=") &&
                            requestPath.contains("grant_type=") &&
                            requestPath.contains("refresh_token=")
                    ) {
                        return new MockResponse()
                                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(HttpStatus.OK.value())
                                .setBody(objectMapper.writeValueAsString(refreshTokenInfo));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when
        StravaOAuthTokenInfo resultTokenInfo = service.refreshAuthenticate("refresh_token");

        //then
        assertThat(resultTokenInfo.getTokenType()).isEqualTo(refreshTokenInfo.getTokenType());
        assertThat(resultTokenInfo.getAccessToken()).isEqualTo(refreshTokenInfo.getAccessToken());
        assertThat(resultTokenInfo.getRefreshToken()).isEqualTo(refreshTokenInfo.getRefreshToken());
        assertThat(resultTokenInfo.getExpiresAt()).isEqualTo(refreshTokenInfo.getExpiresAt());
        assertThat(resultTokenInfo.getExpiresIn()).isEqualTo(refreshTokenInfo.getExpiresIn());
    }

    @Test
    public void onError_INVALID_STRAVA_CLIENT_SECRET() {
        assertThatThrownBy(() -> service.newAuthenticate("abcdefg", "authorization_code")).isInstanceOf(AuthenticationException.class);
        assertThatThrownBy(() -> service.refreshAuthenticate("refresh_token")).isInstanceOf(AuthenticationException.class);
    }
}