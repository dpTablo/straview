package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.dptablo.straview.repository.StravaAthleteRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class StravaAthleteServiceTest {
    private static MockWebServer mockWebServer;

    @InjectMocks
    private StravaAthleteService service;

    @Mock
    private StravaWebClientFactory stravaWebClientFactory;

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
    @WithMockUser(roles = "ADMIN")
    public void getLoggedInAthlete() throws AuthenticationException {
        //given
        given(applicationProperty.getStravaApiV3Timeout()).willReturn(20);
        given(applicationProperty.getStravaApiV3UrlAthlete()).willReturn("/athlete");

        String baseUrl = String.format("http://localhost:%s/api/v3", mockWebServer.getPort());
        String authorization = "Bearer djdfjo290r2c323498c";

        StravaAthlete apiResultAthlete = StravaAthlete.builder()
                .athleteId(8823834L).resourceState(3).firstName("???")
                .lastName("??????").profileMedium("http://abc.com/medium43293.png")
                .profile("http://abc.com/pppproi393.png")
                .city("Seoul")
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorization)
                .build();
        given(stravaWebClientFactory.createApiWebClient()).willReturn(webClient);

        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                try {
                    String requestPath = recordedRequest.getPath();

                    if((requestPath != null && requestPath.startsWith("/api/v3/athlete")) &&
                            authorization.equals(recordedRequest.getHeader(HttpHeaders.AUTHORIZATION))
                    ) {
                        return new MockResponse()
                                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(HttpStatus.OK.value())
                                .setBody(objectMapper.writeValueAsString(apiResultAthlete));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when
        StravaAthlete athlete = service.getLoggedInAthlete();

        //then
        assertThat(athlete.getAthleteId()).isEqualTo(apiResultAthlete.getAthleteId());
        assertThat(athlete.getResourceState()).isEqualTo(apiResultAthlete.getResourceState());
        assertThat(athlete.getFirstName()).isEqualTo(apiResultAthlete.getFirstName());
        assertThat(athlete.getLastName()).isEqualTo(apiResultAthlete.getLastName());
        assertThat(athlete.getProfileMedium()).isEqualTo(apiResultAthlete.getProfileMedium());
        assertThat(athlete.getProfile()).isEqualTo(apiResultAthlete.getProfile());
        assertThat(athlete.getCity()).isEqualTo(apiResultAthlete.getCity());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getLoggedInAthlete_AuthenticationException() throws AuthenticationException {
        //given
        given(applicationProperty.getStravaApiV3Timeout()).willReturn(20);
        given(applicationProperty.getStravaApiV3UrlAthlete()).willReturn("/athlete");

        String baseUrl = String.format("http://localhost:%s/api/v3", mockWebServer.getPort());
        String authorization = "Bearer djdfjo290r2c323498c";

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorization)
                .build();
        given(stravaWebClientFactory.createApiWebClient()).willReturn(webClient);

        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                return new MockResponse().setResponseCode(500);
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when & then
        assertThatThrownBy(() -> service.getLoggedInAthlete()).isInstanceOf(AuthenticationException.class);
    }
}