package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.dto.entity.Gear;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.StravaSyncInfo;
import com.dptablo.straview.dto.entity.SummaryActivity;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.exception.StraviewException;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.dptablo.straview.repository.GearRepository;
import com.dptablo.straview.repository.StravaAthleteRepository;
import com.dptablo.straview.repository.SummaryActivityRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class StravaSummaryActivityServiceTest {
    private static MockWebServer mockWebServer;

    @InjectMocks
    private StravaSummaryActivityService service;

    @Mock
    private StravaWebClientFactory stravaWebClientFactory;

    @Mock
    private SummaryActivityRepository summaryActivityRepository;

    @Mock
    private StravaAthleteRepository athleteRepository;

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
    public void getLoggedInAthleteActivities() throws StraviewException {
        //given
        Long athleteId = getTestAthleteId();

        given(applicationProperty.getStravaApiV3Timeout()).willReturn(20);
        given(applicationProperty.getStravaApiV3UrlAthleteActivities()).willReturn("/athlete/activities");
        given(applicationProperty.getStravaClientAthleteId()).willReturn(athleteId);

        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(athleteId)
                .build();
        given(athleteRepository.findById(applicationProperty.getStravaClientAthleteId())).willReturn(Optional.ofNullable(athlete));

        Gear gear = Gear.builder()
                .manageId(1L)
                .gearId("b9066966")
                .athlete(athlete)
                .build();
        given(gearRepository.findByGearIdAndAthleteId(gear.getGearId(), gear.getAthlete().getAthleteId())).willReturn(Optional.ofNullable(gear));

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
                    HttpUrl requestUrl = recordedRequest.getRequestUrl();

                    String body = "";
                    if(requestPath.startsWith("/api/v3/athlete/activities") &&
                            requestUrl.queryParameter("after").equals("0")
                    ) {
                        body = getActivitiesJsonString();
                    } else {
                        body = "[]";
                    }
                    return new MockResponse()
                            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .setResponseCode(HttpStatus.OK.value())
                            .setBody(body);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        };
        mockWebServer.setDispatcher(dispatcher);

        //when
        StravaSyncInfo syncInfo = StravaSyncInfo.builder()
                .athleteId(applicationProperty.getStravaClientAthleteId())
                .syncEpochTime(0L)
                .build();
        List<SummaryActivity> resultSummaryActivities = service.getLoggedInAthleteActivities(syncInfo.getSyncEpochTime(), 100);

        //then
        assertThat(resultSummaryActivities.size() == 2).isTrue();

        SummaryActivity summaryActivity1 = resultSummaryActivities.get(0);
        assertThat(summaryActivity1.getActivityId()).isEqualTo(6808089325L);
        assertThat(summaryActivity1.getAthlete()).isEqualTo(athlete);

        SummaryActivity summaryActivity2 = resultSummaryActivities.get(1);
        assertThat(summaryActivity2.getActivityId()).isEqualTo(6807930650L);
        assertThat(summaryActivity2.getAthlete()).isEqualTo(athlete);
    }

    private Long getTestAthleteId() {
        return 81334314L;
    }


    private String getActivitiesJsonString() {
        return "[\n" +
                "    {\n" +
                "        \"resource_state\": 2,\n" +
                "        \"athlete\": {\n" +
                "            \"id\": 81334314,\n" +
                "            \"resource_state\": 1\n" +
                "        },\n" +
                "        \"name\": \"Zwift - zone2 in Watopia\",\n" +
                "        \"distance\": 15301.9,\n" +
                "        \"moving_time\": 1826,\n" +
                "        \"elapsed_time\": 1826,\n" +
                "        \"total_elevation_gain\": 73,\n" +
                "        \"type\": \"VirtualRide\",\n" +
                "        \"id\": 6808089325,\n" +
                "        \"start_date\": \"2022-03-11T16:30:45Z\",\n" +
                "        \"start_date_local\": \"2022-03-12T01:30:45Z\",\n" +
                "        \"timezone\": \"(GMT+09:00) Asia/Seoul\",\n" +
                "        \"utc_offset\": 32400,\n" +
                "        \"location_city\": null,\n" +
                "        \"location_state\": null,\n" +
                "        \"location_country\": \"South Korea\",\n" +
                "        \"achievement_count\": 2,\n" +
                "        \"kudos_count\": 0,\n" +
                "        \"comment_count\": 0,\n" +
                "        \"athlete_count\": 6,\n" +
                "        \"photo_count\": 0,\n" +
                "        \"map\": {\n" +
                "            \"id\": \"a6808089325\",\n" +
                "            \"summary_polyline\": \"bg`fA{l}y^lEzLJh@GXaAhA[|AoAfAk@hAUx@?l@v@|At@nC\\\\\\\\j@Fv@SRUd@uAfAI`Ag@h@Dl@h@f@tDM~ABzAURy@BWLQv@AfARn@tC|BdAZtFqBh@XJ^ILaBL{@Ta@\\\\FTPJvAOtDTfASlB{@t@k@^i@z@}DRW`BqAjCeFR{@G}@Uk@uBsBQe@QyAS]k@]s@OW?_@T_ApCmB~B]FuAi@]Ae@Ty@z@SDi@Em@c@q@oBCy@Lo@`AiBvCiAl@?fARPEfBqBDQIa@eAcBYQq@I_AJy@\\\\}@t@{@RmBCiCo@gADqB\\\\wBfAiA`AOZQjAoAjA[f@i@xAAt@r@vAz@vCr@b@lAS`AqB~@CfAk@^Fd@\\\\Tj@XnCKlBBrAELoAJOJOh@GvATp@jCtBr@Zb@BpEgB`@G\\\\PPl@OJmAFuA\\\\QN?XPLzAMfDXhAQdBs@`BuATq@j@yCnA_A`@c@vBcEh@kABa@Es@Yk@sBmBm@wCe@a@aAWc@BYXo@xB{BnC[FyAi@_@?YN}@~@]Fw@QY[m@mBEy@T}@p@qAb@_@~Bu@rBT^UvAaBB_@Oa@gA_B[MqA@m@NeBlA_AN{ACyCo@eBLuAZuBhAe@Ze@p@YzAcAz@e@p@i@vA?t@v@`Bt@fC`@`@l@Fl@QTSh@{A~@EfAi@l@Dd@`@Pj@X~CMnA@~AWP_ABMLQv@AlALd@bDhCp@Rb@EdEcB^GZPPl@OJ_BH{@X]\\\\HTTF~@OhETvAYzAu@t@k@\\\\g@l@aDVi@dBuAtCoFR}@I_Ae@s@iBgBi@oCe@a@aAWc@BSPu@`Cs@fAgAjA]BsAi@]A[L{@|@YJs@Gc@a@k@wAKmAR{@x@}Ab@[pBq@l@AbATPCrByBIs@{@uAe@]q@E}@Jw@\\\\}@t@{@RkBCkCo@sBLuA`@gBz@oAfAWzAwAvAs@tAS`AFd@l@fAx@rCb@b@^F~@STWX_ARY`ACdAk@^Fn@f@f@~DM|ADlAOXuAPU`A?pAJXvC`CbAZb@EvEiB^LPZBLGNsBP{@XOP@VPLtAOzBTlA?dAUfCuAb@e@P_@r@oD^c@tAgApCmFNaAK_AQ[yBuBi@oCe@a@a@M]Ik@F\",\n" +
                "            \"resource_state\": 2\n" +
                "        },\n" +
                "        \"trainer\": false,\n" +
                "        \"commute\": false,\n" +
                "        \"manual\": false,\n" +
                "        \"private\": false,\n" +
                "        \"visibility\": \"everyone\",\n" +
                "        \"flagged\": false,\n" +
                "        \"gear_id\": \"b9066966\",\n" +
                "        \"start_latlng\": [\n" +
                "            -11.63905844092369,\n" +
                "            166.9449484348297\n" +
                "        ],\n" +
                "        \"end_latlng\": [\n" +
                "            -11.645552068948746,\n" +
                "            166.94140791893005\n" +
                "        ],\n" +
                "        \"start_latitude\": -11.63905844092369,\n" +
                "        \"start_longitude\": 166.94140791893005,\n" +
                "        \"average_speed\": 8.38,\n" +
                "        \"max_speed\": 13.49,\n" +
                "        \"average_cadence\": 93.5,\n" +
                "        \"average_watts\": 129.7,\n" +
                "        \"max_watts\": 173,\n" +
                "        \"weighted_average_watts\": 130,\n" +
                "        \"kilojoules\": 236.8,\n" +
                "        \"device_watts\": true,\n" +
                "        \"has_heartrate\": true,\n" +
                "        \"average_heartrate\": 147.9,\n" +
                "        \"max_heartrate\": 155,\n" +
                "        \"heartrate_opt_out\": true,\n" +
                "        \"display_hide_heartrate_option\": true,\n" +
                "        \"elev_high\": 14.8,\n" +
                "        \"elev_low\": 2.2,\n" +
                "        \"upload_id\": 7242175314,\n" +
                "        \"upload_id_str\": \"7242175314\",\n" +
                "        \"external_id\": \"zwift-activity-1032701615770959904.fit\",\n" +
                "        \"from_accepted_tag\": false,\n" +
                "        \"pr_count\": 1,\n" +
                "        \"total_photo_count\": 1,\n" +
                "        \"has_kudoed\": false,\n" +
                "        \"suffer_score\": 46\n" +
                "    },\n" +
                "    {\n" +
                "        \"resource_state\": 2,\n" +
                "        \"athlete\": {\n" +
                "            \"id\": 81334314,\n" +
                "            \"resource_state\": 1\n" +
                "        },\n" +
                "        \"name\": \"Zwift - Ramp Test on Champs-Élysées in Paris\",\n" +
                "        \"distance\": 11433.8,\n" +
                "        \"moving_time\": 1547,\n" +
                "        \"elapsed_time\": 1547,\n" +
                "        \"total_elevation_gain\": 76,\n" +
                "        \"type\": \"VirtualRide\",\n" +
                "        \"id\": 6807930650,\n" +
                "        \"start_date\": \"2022-03-11T16:00:05Z\",\n" +
                "        \"start_date_local\": \"2022-03-12T01:00:05Z\",\n" +
                "        \"timezone\": \"(GMT+09:00) Asia/Seoul\",\n" +
                "        \"utc_offset\": 32400,\n" +
                "        \"location_city\": null,\n" +
                "        \"location_state\": null,\n" +
                "        \"location_country\": \"South Korea\",\n" +
                "        \"achievement_count\": 0,\n" +
                "        \"kudos_count\": 0,\n" +
                "        \"comment_count\": 0,\n" +
                "        \"athlete_count\": 82,\n" +
                "        \"photo_count\": 0,\n" +
                "        \"map\": {\n" +
                "            \"id\": \"a6807930650\",\n" +
                "            \"summary_polyline\": \"qngiH{zbMf@gCbAoElA_GxBuJtBsJFSJQJM^G|@FvDn@JAHEPYb@aBbBaIjH}[`DcODUCSGQUUeAu@gEgCaEuCa@QU?[ROXOl@mExSiGxXMz@Cj@@j@UpDCl@@VBVPf@`@t@r@~@Vd@HTDV@VSxAaHb[iHn\\\\iB~HgAhF}Jnd@sDjPwEnSUh@MJq@Z[ZMPQj@Gr@Bt@DXTn@^\\\\PHR@P?PENK\\\\a@Rm@Hw@?[Iu@WmAA]Hw@|CsMbCuKxOws@|Kgg@rHm]H[PUTITAj@BvDp@R?PKLWx@cDtA{GxDuP|DwQp@oC^gB@]CKMSWScAu@qDuByEiD_@KO@WLKPSl@iArFoBxIgGdYe@~BCf@?z@W|DAl@@TLh@HN^n@p@|@^v@FR@V?TM~@qD`Ps@nDsAvFcElRq@bDqFhVyG~ZkC`LoAbGqFvU\",\n" +
                "            \"resource_state\": 2\n" +
                "        },\n" +
                "        \"trainer\": false,\n" +
                "        \"commute\": false,\n" +
                "        \"manual\": false,\n" +
                "        \"private\": false,\n" +
                "        \"visibility\": \"everyone\",\n" +
                "        \"flagged\": false,\n" +
                "        \"gear_id\": \"b9066966\",\n" +
                "        \"start_latlng\": [\n" +
                "            48.867777585983276,\n" +
                "            2.3135893419384956\n" +
                "        ],\n" +
                "        \"end_latlng\": [\n" +
                "            48.873560428619385,\n" +
                "            2.296261601150036\n" +
                "        ],\n" +
                "        \"start_latitude\": 48.867777585983276,\n" +
                "        \"start_longitude\": 2.296261601150036,\n" +
                "        \"average_speed\": 7.391,\n" +
                "        \"max_speed\": 14.388,\n" +
                "        \"average_cadence\": 89,\n" +
                "        \"average_watts\": 126.4,\n" +
                "        \"max_watts\": 326,\n" +
                "        \"weighted_average_watts\": 164,\n" +
                "        \"kilojoules\": 195.5,\n" +
                "        \"device_watts\": true,\n" +
                "        \"has_heartrate\": true,\n" +
                "        \"average_heartrate\": 144.2,\n" +
                "        \"max_heartrate\": 182,\n" +
                "        \"heartrate_opt_out\": true,\n" +
                "        \"display_hide_heartrate_option\": true,\n" +
                "        \"elev_high\": 71.4,\n" +
                "        \"elev_low\": 41.4,\n" +
                "        \"upload_id\": 7242005203,\n" +
                "        \"upload_id_str\": \"7242005203\",\n" +
                "        \"external_id\": \"zwift-activity-1032686111828246576.fit\",\n" +
                "        \"from_accepted_tag\": false,\n" +
                "        \"pr_count\": 0,\n" +
                "        \"total_photo_count\": 0,\n" +
                "        \"has_kudoed\": false,\n" +
                "        \"suffer_score\": 30\n" +
                "    }\n" +
                "]";
    }
}