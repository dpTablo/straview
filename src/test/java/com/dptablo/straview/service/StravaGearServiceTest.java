package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.Gear;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StravaApiException;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.dptablo.straview.repository.GearRepository;
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
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class StravaGearServiceTest {
    private static MockWebServer mockWebServer;

    @InjectMocks
    private StravaGearService gearService;

    @Mock
    private StravaWebClientFactory stravaWebClientFactory;

    @Mock
    private GearRepository gearRepository;

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
    public void getGearById() throws AuthenticationException, StravaApiException {
        //given
        given(applicationProperty.getStravaApiV3UrlGear()).willReturn("/gear");

        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(334455L)
                .build();

        Gear responseGear1 = Gear.builder()
                .gearId("b88467")
                .athlete(athlete)
                .build();

        Gear responseGear2 = Gear.builder()
                .gearId("b833567")
                .athlete(athlete)
                .build();
        List<Gear> returnedGearList = Arrays.asList(responseGear1, responseGear2);
        given(gearRepository.saveAll(anyList())).willReturn(returnedGearList);

        List<String> gearIdList = Arrays.asList(responseGear1.getGearId(), responseGear2.getGearId());

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
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                try {
                    String requestPath = recordedRequest.getPath();
                    String gearId = requestPath.substring(requestPath.lastIndexOf("/") + 1);

                    if((requestPath != null && requestPath.startsWith("/api/v3/gear")) &&
                            authorization.equals(recordedRequest.getHeader(HttpHeaders.AUTHORIZATION))
                    ) {
                        if(gearId.equals(responseGear1.getGearId())) {
                            return new MockResponse()
                                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .setResponseCode(HttpStatus.OK.value())
                                    .setBody(objectMapper.writeValueAsString(responseGear1));
                        } else if(gearId.equals(responseGear2.getGearId())) {
                            return new MockResponse()
                                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .setResponseCode(HttpStatus.OK.value())
                                    .setBody(objectMapper.writeValueAsString(responseGear2));
                        } else {
                            return new MockResponse()
                                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .setResponseCode(HttpStatus.OK.value())
                                    .setBody("");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when
        List<Gear> savedGears = gearService.getGearById(athlete, gearIdList);

        //then
        verify(gearRepository, times(1)).saveAll(anyList());
        assertThat(savedGears.size()).isEqualTo(2);

        Gear savedGear1 = savedGears.get(0);
        assertThat(savedGear1.getGearId()).isEqualTo(responseGear1.getGearId());
        assertThat(savedGear1.getAthlete()).isEqualTo(responseGear1.getAthlete());

        Gear savedGear2 = savedGears.get(0);
        assertThat(savedGear2.getGearId()).isEqualTo(responseGear1.getGearId());
        assertThat(savedGear2.getAthlete()).isEqualTo(responseGear1.getAthlete());
    }
}