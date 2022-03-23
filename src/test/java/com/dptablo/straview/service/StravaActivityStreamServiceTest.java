package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.*;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class StravaActivityStreamServiceTest {
    private static MockWebServer mockWebServer;

    @InjectMocks
    private StravaActivityStreamService activityStreamService;

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

    @BeforeEach
    private void beforeEach() throws AuthenticationException {
        String baseUrl = String.format("http://localhost:%s/api/v3", mockWebServer.getPort());
        String authorization = "Bearer djdfjo290r2c323498c";

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize((1 * 1024 * 1024) * 15))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorization)
                .build();
        given(stravaWebClientFactory.createApiWebClient()).willReturn(webClient);
    }

    @Test
    public void getActivityStreams() throws IOException, AuthenticationException {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .manageId(489892892L)
                .activityId(123312123L)
                .build();

        List<ActivityStreamType> activityStreamTypes = Arrays.asList(
                ActivityStreamType.DISTANCE, ActivityStreamType.WATTS, ActivityStreamType.HEARTRATE,
                ActivityStreamType.CADENCE, ActivityStreamType.MOVING, ActivityStreamType.TIME,
                ActivityStreamType.LATLNG, ActivityStreamType.ALTITUDE, ActivityStreamType.TEMP,
                ActivityStreamType.VELOCITY_SMOOTH, ActivityStreamType.GRADE_SMOOTH);

        given(applicationProperty.getStravaApiV3UrlActivitiesStreams(activity.getActivityId()))
                .willReturn("/activities/" + activity.getActivityId().toString() + "/streams");

        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                try {
                    String requestPath = recordedRequest.getPath();
                    String streamPath = applicationProperty.getStravaApiV3UrlActivitiesStreams(activity.getActivityId());
                    String keysQueryString = recordedRequest.getRequestUrl().queryParameter("keys");
                    String keyByTypeQueryString = recordedRequest.getRequestUrl().queryParameter("key_by_type");

                    if((requestPath != null && requestPath.startsWith(streamPath)) &&
                            !keysQueryString.isEmpty() || keyByTypeQueryString.equals("true")
                    ) {
                        return new MockResponse()
                                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(HttpStatus.OK.value())
                                .setBody(readActivityStreamSampleJson());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when
        List<ActivityStream> activityStreams =
                activityStreamService.getActivityStreams(activity, activityStreamTypes);

        //then
        assertThat(activityStreams.size()).isEqualTo(11);

        activityStreams.forEach(activityStream -> {
            assertThat(activityStream.getSummaryActivity()).isEqualTo(activity);
            assertThat(activityStream.getType()).isNotNull();
        });

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamDistance)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamMoving)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamLatlng)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamTime)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamAltitude)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamGradeSmooth)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamCadence)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamHeartrate)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamWatts)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamVelocitySmooth)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();

        assertThat(
                activityStreams.stream()
                        .filter(activityStream -> activityStream instanceof ActivityStreamTemp)
                        .findFirst().orElseThrow(NullPointerException::new)
        ).isNotNull();
    }

    private String readActivityStreamSampleJson() throws IOException {
        Path path = Paths.get("src/test/java/com/dptablo/straview/service/activity_stream_sample.json");

        StringBuilder builder = new StringBuilder();
        try(Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> builder.append(s).append("\n"));
        } catch (IOException e) {
            throw e;
        }
        return builder.toString();
    }
}