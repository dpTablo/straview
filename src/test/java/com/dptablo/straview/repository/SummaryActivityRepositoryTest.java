package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.Gear;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.SummaryActivity;
import com.dptablo.straview.dto.enumtype.ResourceState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SummaryActivityRepositoryTest {
    @Autowired
    private SummaryActivityRepository summaryActivityRepository;

    @Test
    public void jsonToObject() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"resource_state\": 2,\n" +
                "    \"athlete\": {\n" +
                "        \"id\": 81334314,\n" +
                "        \"resource_state\": 4\n" +
                "    },\n" +
                "    \"name\": \"Zwift - zone2 on Tour Of Tewit Well in Yorkshire\",\n" +
                "    \"distance\": 22102,\n" +
                "    \"moving_time\": 3781,\n" +
                "    \"elapsed_time\": 3781,\n" +
                "    \"total_elevation_gain\": 409,\n" +
                "    \"type\": \"VirtualRide\",\n" +
                "    \"id\": 6802915857,\n" +
                "    \"start_date\": \"2022-03-10T14:53:40Z\",\n" +
                "    \"start_date_local\": \"2022-03-10T23:53:40Z\",\n" +
                "    \"timezone\": \"(GMT+09:00) Asia/Seoul\",\n" +
                "    \"utc_offset\": 32400,\n" +
                "    \"location_city\": \"Seoul\",\n" +
                "    \"location_state\": \"none\",\n" +
                "    \"location_country\": \"South Korea\",\n" +
                "    \"achievement_count\": 10,\n" +
                "    \"kudos_count\": 0,\n" +
                "    \"comment_count\": 0,\n" +
                "    \"athlete_count\": 4,\n" +
                "    \"photo_count\": 0,\n" +
                "    \"map\": {\n" +
                "        \"id\": \"a6802915857\",\n" +
                "        \"summary_polyline\": \"msohIhzkHgEhC}@d@w@ZiBn@cGhBkD~@y@XcAt@{DdDm@p@Of@Dj@jBlIVx@Zj@JVNp@LNNFNAN@LJtBvCdCbD|@bApAhA~C~B|Ax@|Bx@PNNV\\\\t@Tv@bAtErBdKx@pDZhAn@jBfAdCbBbDdAzBn@jA\\\\v@DN@ZGXOVeIzMy@nAcA~@yBbBs@d@u@`@qA^[JSZs@`B{@nCaGlQOj@Gr@HhB\\\\lEj@|FVxC?r@Qj@]L[McAwAUSYCo@Di@JQHSHu@`A}FpIGLEb@?d@JlBXxDFlA@bAWzHYbOu@h[Dr@DNT^RLJ@rAJxIf@`Jj@RB|@V|@b@PLd@d@b@n@xAhCh@v@l@p@p@f@fBz@jEdB`IvDnAd@`@Zb@`@TJR?h@ER@RJr@nAJJJIDKLo@L_AJc@NOL?LFh@`@zAvAj@\\\\b@Lb@FbADXD`DJ|AH~Eh@hRbDtCb@PCF]I_A_@aC_@}CyDi]uC}W}AkOkJo{@oAcLuBkQiBuQQcA_Lil@eHm_@iOyw@I_@O[UOSEU?SHeIrFoAv@qAn@wBv@cFzA}EtAg@PSJ{AfA_DnCm@r@Mh@Fj@tBfJr@|ANp@LPPDPANDLL~A|BpCnDhApAv@p@nDnC`Ah@v@\\\\bBj@PLNVh@nANl@x@tDhB`Jz@|Dd@fBb@tAx@rBtGzMFf@I^a@r@uAvBoDbG_BdCaA`AsCxBu@d@]NuA`@[NUZw@lBaBfFuDvKc@tAMl@Et@Dt@R`DnAxMBv@Kp@]V_@IY]o@_AYSYA[Bo@JWHSJa@f@sGjJGNGj@@h@j@pI@pBOjE]bQw@p[APFt@LXLRHFPFl@DvKl@lIh@TBz@Vz@b@d@^t@`AbAhBx@pAj@l@l@f@bB|@dFrBfGxC`CbAPJt@n@RJT@h@GTBPJt@rAN@FMNo@LaAJg@JKNCVJh@d@`A`Al@`@n@Xx@FbHXtCV|Dh@rEz@tKhBjAPRG@_@A_@y@eGcAsIyFoh@}AiO{KkcAyAwMoAgKaBiPUgBg_@epByEqVM_@QUUKUASDaJfGaB|@wB|@sH|B\",\n" +
                "        \"resource_state\": 2\n" +
                "    },\n" +
                "    \"trainer\": false,\n" +
                "    \"commute\": false,\n" +
                "    \"manual\": false,\n" +
                "    \"private\": false,\n" +
                "    \"visibility\": \"everyone\",\n" +
                "    \"flagged\": false,\n" +
                "    \"gear_id\": \"b9066966\",\n" +
                "    \"start_latlng\": [\n" +
                "        53.98855447769165,\n" +
                "        -1.5403691679239273\n" +
                "    ],\n" +
                "    \"end_latlng\": [\n" +
                "        53.991917967796326,\n" +
                "        -1.5421291999518871\n" +
                "    ],\n" +
                "    \"start_latitude\": 53.98855447769165,\n" +
                "    \"start_longitude\": -1.5421291999518871,\n" +
                "    \"average_speed\": 5.846,\n" +
                "    \"max_speed\": 18.348,\n" +
                "    \"average_cadence\": 99.7,\n" +
                "    \"average_watts\": 119.2,\n" +
                "    \"max_watts\": 215,\n" +
                "    \"weighted_average_watts\": 120,\n" +
                "    \"kilojoules\": 450.8,\n" +
                "    \"device_watts\": true,\n" +
                "    \"has_heartrate\": true,\n" +
                "    \"average_heartrate\": 147.1,\n" +
                "    \"max_heartrate\": 156,\n" +
                "    \"heartrate_opt_out\": true,\n" +
                "    \"display_hide_heartrate_option\": true,\n" +
                "    \"elev_high\": 182.6,\n" +
                "    \"elev_low\": 97,\n" +
                "    \"upload_id\": 7236610091,\n" +
                "    \"upload_id_str\": \"7236610091\",\n" +
                "    \"external_id\": \"zwift-activity-1031927891740327968.fit\",\n" +
                "    \"from_accepted_tag\": false,\n" +
                "    \"pr_count\": 5,\n" +
                "    \"total_photo_count\": 1,\n" +
                "    \"has_kudoed\": false,\n" +
                "    \"suffer_score\": 89\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        SummaryActivity summaryActivity = objectMapper.readValue(jsonString, SummaryActivity.class);

        //then
        assertThat(summaryActivity).isNotNull();
        assertThat(summaryActivity.getGearId()).isEqualTo(jsonNode.get("gear_id").asText());
        assertThat(summaryActivity.getActivityId()).isEqualTo(jsonNode.get("id").asLong());
        assertThat(summaryActivity.getResourceState().getValue()).isEqualTo(jsonNode.get("resource_state").asInt());
        assertThat(summaryActivity.getName()).isEqualTo(jsonNode.get("name").asText());
        assertThat(summaryActivity.getDistance())
                .isEqualTo(Double.valueOf(jsonNode.get("distance").asDouble()).floatValue());
        assertThat(summaryActivity.getMovingTime()).isEqualTo(jsonNode.get("moving_time").asInt());
        assertThat(summaryActivity.getElapsedTime()).isEqualTo(jsonNode.get("elapsed_time").asInt());
        assertThat(summaryActivity.getTotalElevationGain())
                .isEqualTo(Double.valueOf(jsonNode.get("total_elevation_gain").asDouble()).floatValue());
        assertThat(summaryActivity.getType()).isEqualTo(jsonNode.get("type").asText());
        assertThat(summaryActivity.getStartDate()).isEqualTo(jsonNode.get("start_date").asText());
        assertThat(summaryActivity.getStartDateLocal()).isEqualTo(jsonNode.get("start_date_local").asText());
        assertThat(summaryActivity.getTimezone()).isEqualTo(jsonNode.get("timezone").asText());
        assertThat(summaryActivity.getUtcOffset()).isEqualTo(jsonNode.get("utc_offset").asInt());
        assertThat(summaryActivity.getLocationCity()).isEqualTo(jsonNode.get("location_city").asText());
        assertThat(summaryActivity.getLocationState()).isEqualTo(jsonNode.get("location_state").asText());
        assertThat(summaryActivity.getLocationCountry()).isEqualTo(jsonNode.get("location_country").asText());
        assertThat(summaryActivity.getAchievementCount()).isEqualTo(jsonNode.get("achievement_count").asInt());
        assertThat(summaryActivity.getKudosCount()).isEqualTo(jsonNode.get("kudos_count").asInt());
        assertThat(summaryActivity.getCommentCount()).isEqualTo(jsonNode.get("comment_count").asInt());
        assertThat(summaryActivity.getAthleteCount()).isEqualTo(jsonNode.get("athlete_count").asInt());
        assertThat(summaryActivity.getPhotoCount()).isEqualTo(jsonNode.get("photo_count").asInt());
        assertThat(summaryActivity.getPrCount()).isEqualTo(jsonNode.get("pr_count").asInt());
        assertThat(summaryActivity.getTotalPhotoCount()).isEqualTo(jsonNode.get("total_photo_count").asInt());
        assertThat(summaryActivity.getTrainer()).isEqualTo(jsonNode.get("trainer").asBoolean());
        assertThat(summaryActivity.getCommute()).isEqualTo(jsonNode.get("commute").asBoolean());
        assertThat(summaryActivity.getManual()).isEqualTo(jsonNode.get("manual").asBoolean());
        assertThat(summaryActivity.getPrivateFlag()).isEqualTo(jsonNode.get("private").asBoolean());
        assertThat(summaryActivity.getVisibility()).isEqualTo(jsonNode.get("visibility").asText());
        assertThat(summaryActivity.getFlagged()).isEqualTo(jsonNode.get("flagged").asBoolean());

        JsonNode start_latlng = jsonNode.get("start_latlng");
        for(int i = 0; i < summaryActivity.getStartLatlng().size(); i++) {
            assertThat(summaryActivity.getStartLatlng().get(i))
                    .isEqualTo(Double.valueOf(start_latlng.get(i).asDouble()).floatValue());
        }

        JsonNode end_latlng = jsonNode.get("end_latlng");
        for(int i = 0; i < summaryActivity.getEndLatlng().size(); i++) {
            assertThat(summaryActivity.getEndLatlng().get(i))
                    .isEqualTo(Double.valueOf(end_latlng.get(i).asDouble()).floatValue());
        }

        assertThat(summaryActivity.getStartLatitude())
                .isEqualTo(Double.valueOf(jsonNode.get("start_latitude").asDouble()).floatValue());
        assertThat(summaryActivity.getStartLongitude())
                .isEqualTo(Double.valueOf(jsonNode.get("start_longitude").asDouble()).floatValue());
        assertThat(summaryActivity.getAverageSpeed())
                .isEqualTo(Double.valueOf(jsonNode.get("average_speed").asDouble()).floatValue());
        assertThat(summaryActivity.getMaxSpeed())
                .isEqualTo(Double.valueOf(jsonNode.get("max_speed").asDouble()).floatValue());
        assertThat(summaryActivity.getAverageCadence())
                .isEqualTo(Double.valueOf(jsonNode.get("average_cadence").asDouble()).floatValue());
        assertThat(summaryActivity.getAverageWatts())
                .isEqualTo(Double.valueOf(jsonNode.get("average_watts").asDouble()).floatValue());
        assertThat(summaryActivity.getWeightedAverageWatts())
                .isEqualTo(Double.valueOf(jsonNode.get("weighted_average_watts").asDouble()).floatValue());
        assertThat(summaryActivity.getMaxWatts())
                .isEqualTo(Double.valueOf(jsonNode.get("max_watts").asDouble()).floatValue());
        assertThat(summaryActivity.getKilojoules())
                .isEqualTo(Double.valueOf(jsonNode.get("kilojoules").asDouble()).floatValue());
        assertThat(summaryActivity.getDeviceWatts()).isEqualTo(jsonNode.get("device_watts").asBoolean());
        assertThat(summaryActivity.getHasHeartrate()).isEqualTo(jsonNode.get("has_heartrate").asBoolean());
        assertThat(summaryActivity.getElevHigh())
                .isEqualTo(Double.valueOf(jsonNode.get("elev_high").asDouble()).floatValue());
        assertThat(summaryActivity.getElevLow())
                .isEqualTo(Double.valueOf(jsonNode.get("elev_low").asDouble()).floatValue());
        assertThat(summaryActivity.getUploadId()).isEqualTo(jsonNode.get("upload_id").asLong());
        assertThat(summaryActivity.getExternalId()).isEqualTo(jsonNode.get("external_id").asText());
        assertThat(summaryActivity.getHasKudoed()).isEqualTo(jsonNode.get("has_kudoed").asBoolean());
    }

    @Test
    public void save() {
        //given
        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(1453435L)
                .userName("user A")
                .build();

        Gear gear = Gear.builder()
                .id(12)
                .gearId("b903483")
                .athlete(athlete)
                .build();

        SummaryActivity summaryActivity = SummaryActivity.builder()
                .activityId(12323L)
                .athlete(athlete)
                .resourceState(ResourceState.SUMMARY)
                .name("Zwift - zone2 on Tour Of Tewit Well in Yorkshire")
                .distance(22102F)
                .movingTime(3781)
                .elapsedTime(3781)
                .totalElevationGain(490F)
                .type("VirtualRide")
                .startDate("2022-03-10T14:53:40Z")
                .startDateLocal("2022-03-10T23:53:40Z")
                .timezone("(GMT+09:00) Asia/Seoul")
                .utcOffset(32400)
                .locationCity("seoul")
                .locationState("empty")
                .locationCountry("South Korea")
                .achievementCount(10)
                .kudosCount(1)
                .commentCount(2)
                .athleteCount(4)
                .photoCount(0)
                .prCount(5)
                .totalPhotoCount(1)
                .trainer(false)
                .commute(false)
                .manual(false)
                .privateFlag(false)
                .visibility("everyone")
                .flagged(false)
                .gearId(gear.getGearId())
                .startLatlng(Arrays.asList(53.98855447769165f, -1.5403691679239273f))
                .endLatlng(Arrays.asList(53.991917967796326f, -1.5421291999518871f))
                .startLatitude(53.98855447769165F)
                .startLongitude(-1.5421291999518871F)
                .averageSpeed(5.846F)
                .maxSpeed(18.348F)
                .averageCadence(99.7F)
                .averageWatts(119.2F)
                .weightedAverageWatts(120F)
                .maxWatts(215F)
                .kilojoules(450.8F)
                .deviceWatts(true)
                .hasHeartrate(true)
                .elevHigh(182.6F)
                .elevLow(97F)
                .uploadId(7236610091L)
                .externalId("zwift-activity-1031927891740327968.fit")
                .hasKudoed(false)
                .gear(gear)
                .build();

        //when
        SummaryActivity savedSummaryActivity = summaryActivityRepository.save(summaryActivity);

        //then
        SummaryActivity foundSummaryActivity = summaryActivityRepository.findById(savedSummaryActivity.getActivityId())
                .orElseThrow(NullPointerException::new);
        assertThat(foundSummaryActivity.getGearId()).isEqualTo(savedSummaryActivity.getGearId());
        assertThat(foundSummaryActivity.getActivityId()).isEqualTo(savedSummaryActivity.getActivityId());
        assertThat(foundSummaryActivity.getResourceState()).isEqualTo(savedSummaryActivity.getResourceState());
        assertThat(foundSummaryActivity.getName()).isEqualTo(savedSummaryActivity.getName());
        assertThat(foundSummaryActivity.getDistance()).isEqualTo(savedSummaryActivity.getDistance());
        assertThat(foundSummaryActivity.getMovingTime()).isEqualTo(savedSummaryActivity.getMovingTime());
        assertThat(foundSummaryActivity.getElapsedTime()).isEqualTo(savedSummaryActivity.getElapsedTime());
        assertThat(foundSummaryActivity.getTotalElevationGain()).isEqualTo(savedSummaryActivity.getTotalElevationGain());
        assertThat(foundSummaryActivity.getType()).isEqualTo(savedSummaryActivity.getType());
        assertThat(foundSummaryActivity.getStartDate()).isEqualTo(savedSummaryActivity.getStartDate());
        assertThat(foundSummaryActivity.getStartDateLocal()).isEqualTo(savedSummaryActivity.getStartDateLocal());
        assertThat(foundSummaryActivity.getTimezone()).isEqualTo(savedSummaryActivity.getTimezone());
        assertThat(foundSummaryActivity.getUtcOffset()).isEqualTo(savedSummaryActivity.getUtcOffset());
        assertThat(foundSummaryActivity.getLocationCity()).isEqualTo(savedSummaryActivity.getLocationCity());
        assertThat(foundSummaryActivity.getLocationState()).isEqualTo(savedSummaryActivity.getLocationState());
        assertThat(foundSummaryActivity.getLocationCountry()).isEqualTo(savedSummaryActivity.getLocationCountry());
        assertThat(foundSummaryActivity.getAchievementCount()).isEqualTo(savedSummaryActivity.getAchievementCount());
        assertThat(foundSummaryActivity.getKudosCount()).isEqualTo(savedSummaryActivity.getKudosCount());
        assertThat(foundSummaryActivity.getCommentCount()).isEqualTo(savedSummaryActivity.getCommentCount());
        assertThat(foundSummaryActivity.getAthleteCount()).isEqualTo(savedSummaryActivity.getAthleteCount());
        assertThat(foundSummaryActivity.getPhotoCount()).isEqualTo(savedSummaryActivity.getPhotoCount());
        assertThat(foundSummaryActivity.getPrCount()).isEqualTo(savedSummaryActivity.getPrCount());
        assertThat(foundSummaryActivity.getTotalPhotoCount()).isEqualTo(savedSummaryActivity.getTotalPhotoCount());
        assertThat(foundSummaryActivity.getTrainer()).isEqualTo(savedSummaryActivity.getTrainer());
        assertThat(foundSummaryActivity.getCommute()).isEqualTo(savedSummaryActivity.getCommute());
        assertThat(foundSummaryActivity.getManual()).isEqualTo(savedSummaryActivity.getManual());
        assertThat(foundSummaryActivity.getPrivateFlag()).isEqualTo(savedSummaryActivity.getPrivateFlag());
        assertThat(foundSummaryActivity.getVisibility()).isEqualTo(savedSummaryActivity.getVisibility());
        assertThat(foundSummaryActivity.getFlagged()).isEqualTo(savedSummaryActivity.getFlagged());
        assertThat(foundSummaryActivity.getStartLatlng()).isEqualTo(savedSummaryActivity.getStartLatlng());
        assertThat(foundSummaryActivity.getEndLatlng()).isEqualTo(savedSummaryActivity.getEndLatlng());
        assertThat(foundSummaryActivity.getStartLatitude()).isEqualTo(savedSummaryActivity.getStartLatitude());
        assertThat(foundSummaryActivity.getStartLongitude()).isEqualTo(savedSummaryActivity.getStartLongitude());
        assertThat(foundSummaryActivity.getAverageSpeed()).isEqualTo(savedSummaryActivity.getAverageSpeed());
        assertThat(foundSummaryActivity.getMaxSpeed()).isEqualTo(savedSummaryActivity.getMaxSpeed());
        assertThat(foundSummaryActivity.getAverageCadence()).isEqualTo(savedSummaryActivity.getAverageCadence());
        assertThat(foundSummaryActivity.getAverageWatts()).isEqualTo(savedSummaryActivity.getAverageWatts());
        assertThat(foundSummaryActivity.getWeightedAverageWatts()).isEqualTo(savedSummaryActivity.getWeightedAverageWatts());
        assertThat(foundSummaryActivity.getMaxWatts()).isEqualTo(savedSummaryActivity.getMaxWatts());
        assertThat(foundSummaryActivity.getKilojoules()).isEqualTo(savedSummaryActivity.getKilojoules());
        assertThat(foundSummaryActivity.getDeviceWatts()).isEqualTo(savedSummaryActivity.getDeviceWatts());
        assertThat(foundSummaryActivity.getHasHeartrate()).isEqualTo(savedSummaryActivity.getHasHeartrate());
        assertThat(foundSummaryActivity.getElevHigh()).isEqualTo(savedSummaryActivity.getElevHigh());
        assertThat(foundSummaryActivity.getElevLow()).isEqualTo(savedSummaryActivity.getElevLow());
        assertThat(foundSummaryActivity.getUploadId()).isEqualTo(savedSummaryActivity.getUploadId());
        assertThat(foundSummaryActivity.getExternalId()).isEqualTo(savedSummaryActivity.getExternalId());
        assertThat(foundSummaryActivity.getHasKudoed()).isEqualTo(savedSummaryActivity.getHasKudoed());
        assertThat(foundSummaryActivity.getAthlete()).isEqualTo(savedSummaryActivity.getAthlete());
        assertThat(foundSummaryActivity.getGear()).isEqualTo(savedSummaryActivity.getGear());
    }
}