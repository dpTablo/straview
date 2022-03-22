package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.*;
import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ActivityStreamRepositoryTest {
    @Autowired
    private ActivityStreamRepository<ActivityStreamDistance> activityStreamDistanceRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamHeartrate> activityStreamHeartrateRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamWatts> activityStreamWattsActivityStreamRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamMoving> movingActivityStreamRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamCadence> cadenceActivityStreamRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamVelocitySmooth> velocitySmoothActivityStreamRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamTime> timeActivityStreamRepository;

    @Autowired
    private ActivityStreamRepository<ActivityStreamAltitude> altitudeActivityStreamRepository;



    @Test
    public void save_distance() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamDistance distanceStream = ActivityStreamDistance.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(5L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(139, 172, 172, 173, 114))
                .build();

        //when
        ActivityStreamDistance savedDistanceStream = activityStreamDistanceRepository.save(distanceStream);
        ActivityStreamDistance foundDistanceStream = activityStreamDistanceRepository.findById(savedDistanceStream.getStreamId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundDistanceStream.getType()).isEqualTo(distanceStream.getType());
        assertThat(foundDistanceStream.getSummaryActivity()).isEqualTo(distanceStream.getSummaryActivity());
        assertThat(foundDistanceStream.getData()).isEqualTo(distanceStream.getData());
        assertThat(foundDistanceStream.getOriginalSize()).isEqualTo(distanceStream.getOriginalSize());
        assertThat(foundDistanceStream.getSeriesType()).isEqualTo(distanceStream.getSeriesType());
        assertThat(foundDistanceStream.getResolution()).isEqualTo(distanceStream.getResolution());
    }

    @Test
    public void save_heartrate() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamHeartrate heartrateStream = ActivityStreamHeartrate.builder()
                .type(ActivityStreamType.HEARTRATE)
                .summaryActivity(activity)
                .originalSize(5L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(120, 121, 122, 134, 140))
                .build();

        //when
        ActivityStreamHeartrate savedHeartrateStream = activityStreamHeartrateRepository.save(heartrateStream);
        ActivityStreamHeartrate foundHeartrateStream = activityStreamHeartrateRepository.findById(savedHeartrateStream.getStreamId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundHeartrateStream.getType()).isEqualTo(heartrateStream.getType());
        assertThat(foundHeartrateStream.getSummaryActivity()).isEqualTo(heartrateStream.getSummaryActivity());
        assertThat(foundHeartrateStream.getData()).isEqualTo(heartrateStream.getData());
        assertThat(foundHeartrateStream.getOriginalSize()).isEqualTo(heartrateStream.getOriginalSize());
        assertThat(foundHeartrateStream.getSeriesType()).isEqualTo(heartrateStream.getSeriesType());
        assertThat(foundHeartrateStream.getResolution()).isEqualTo(heartrateStream.getResolution());
    }

    @Test
    public void save_watts() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamWatts wattsStream = ActivityStreamWatts.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(5L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(120, 121, 122, 134, 140))
                .build();

        //when
        ActivityStreamWatts savedWattsStream = activityStreamWattsActivityStreamRepository.save(wattsStream);
        ActivityStreamWatts foundWattsStream = activityStreamWattsActivityStreamRepository.findById(savedWattsStream.getStreamId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundWattsStream.getType()).isEqualTo(wattsStream.getType());
        assertThat(foundWattsStream.getSummaryActivity()).isEqualTo(wattsStream.getSummaryActivity());
        assertThat(foundWattsStream.getData()).isEqualTo(wattsStream.getData());
        assertThat(foundWattsStream.getOriginalSize()).isEqualTo(wattsStream.getOriginalSize());
        assertThat(foundWattsStream.getSeriesType()).isEqualTo(wattsStream.getSeriesType());
        assertThat(foundWattsStream.getResolution()).isEqualTo(wattsStream.getResolution());
    }

    @Test
    public void save_moving() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamMoving movingStream = ActivityStreamMoving.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(4L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(true, false, false, true))
                .build();

        //when
        ActivityStreamMoving savedMovingStream = movingActivityStreamRepository.save(movingStream);
        ActivityStreamMoving foundMovingStream = movingActivityStreamRepository.findById(savedMovingStream.getStreamId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundMovingStream.getType()).isEqualTo(movingStream.getType());
        assertThat(foundMovingStream.getSummaryActivity()).isEqualTo(movingStream.getSummaryActivity());
        assertThat(foundMovingStream.getData()).isEqualTo(movingStream.getData());
        assertThat(foundMovingStream.getOriginalSize()).isEqualTo(movingStream.getOriginalSize());
        assertThat(foundMovingStream.getSeriesType()).isEqualTo(movingStream.getSeriesType());
        assertThat(foundMovingStream.getResolution()).isEqualTo(movingStream.getResolution());
    }

    @Test
    public void save_cadence() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamCadence cadenceStream = ActivityStreamCadence.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(4L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(75, 76, 77, 88))
                .build();

        //when
        ActivityStreamCadence savedCadenceStream = cadenceActivityStreamRepository.save(cadenceStream);
        ActivityStreamCadence foundCadenceStream = cadenceActivityStreamRepository.findById(savedCadenceStream.getStreamId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundCadenceStream.getType()).isEqualTo(cadenceStream.getType());
        assertThat(foundCadenceStream.getSummaryActivity()).isEqualTo(cadenceStream.getSummaryActivity());
        assertThat(foundCadenceStream.getData()).isEqualTo(cadenceStream.getData());
        assertThat(foundCadenceStream.getOriginalSize()).isEqualTo(cadenceStream.getOriginalSize());
        assertThat(foundCadenceStream.getSeriesType()).isEqualTo(cadenceStream.getSeriesType());
        assertThat(foundCadenceStream.getResolution()).isEqualTo(cadenceStream.getResolution());
    }

    @Test
    public void save_velocitySmooth() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamVelocitySmooth velocitySmoothStream = ActivityStreamVelocitySmooth.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(5L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(5.5f, 6.7f, 7.8f, 8.8f, 9.0f))
                .build();

        //when
        ActivityStreamVelocitySmooth savedVelocitySmoothStream =
                velocitySmoothActivityStreamRepository.save(velocitySmoothStream);
        ActivityStreamVelocitySmooth foundVelocitySmoothStream =
                velocitySmoothActivityStreamRepository.findById(savedVelocitySmoothStream.getStreamId())
                    .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundVelocitySmoothStream.getType()).isEqualTo(velocitySmoothStream.getType());
        assertThat(foundVelocitySmoothStream.getSummaryActivity()).isEqualTo(velocitySmoothStream.getSummaryActivity());
        assertThat(foundVelocitySmoothStream.getData()).isEqualTo(velocitySmoothStream.getData());
        assertThat(foundVelocitySmoothStream.getOriginalSize()).isEqualTo(velocitySmoothStream.getOriginalSize());
        assertThat(foundVelocitySmoothStream.getSeriesType()).isEqualTo(velocitySmoothStream.getSeriesType());
        assertThat(foundVelocitySmoothStream.getResolution()).isEqualTo(velocitySmoothStream.getResolution());
    }

    @Test
    public void save_time() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamTime timeStream = ActivityStreamTime.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(4L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(1, 2, 3, 4))
                .build();

        //when
        ActivityStreamTime savedCadenceStream = timeActivityStreamRepository.save(timeStream);
        ActivityStreamTime foundCadenceStream = timeActivityStreamRepository.findById(savedCadenceStream.getStreamId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundCadenceStream.getType()).isEqualTo(timeStream.getType());
        assertThat(foundCadenceStream.getSummaryActivity()).isEqualTo(timeStream.getSummaryActivity());
        assertThat(foundCadenceStream.getData()).isEqualTo(timeStream.getData());
        assertThat(foundCadenceStream.getOriginalSize()).isEqualTo(timeStream.getOriginalSize());
        assertThat(foundCadenceStream.getSeriesType()).isEqualTo(timeStream.getSeriesType());
        assertThat(foundCadenceStream.getResolution()).isEqualTo(timeStream.getResolution());
    }

    @Test
    public void save_altitude() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamAltitude altitudeStream = ActivityStreamAltitude.builder()
                .type(ActivityStreamType.WATTS)
                .summaryActivity(activity)
                .originalSize(5L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(5.5f, 6.7f, 7.8f, 8.8f, 9.0f))
                .build();

        //when
        ActivityStreamAltitude savedAltitudeStream = altitudeActivityStreamRepository.save(altitudeStream);
        ActivityStreamAltitude foundAltitudeStream =
                altitudeActivityStreamRepository.findById(savedAltitudeStream.getStreamId())
                        .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundAltitudeStream.getType()).isEqualTo(altitudeStream.getType());
        assertThat(foundAltitudeStream.getSummaryActivity()).isEqualTo(altitudeStream.getSummaryActivity());
        assertThat(foundAltitudeStream.getData()).isEqualTo(altitudeStream.getData());
        assertThat(foundAltitudeStream.getOriginalSize()).isEqualTo(altitudeStream.getOriginalSize());
        assertThat(foundAltitudeStream.getSeriesType()).isEqualTo(altitudeStream.getSeriesType());
        assertThat(foundAltitudeStream.getResolution()).isEqualTo(altitudeStream.getResolution());
    }

    @Test
    public void jsonToObject_distance() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"distance\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [110,120,130,140,150]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamDistance distance = objectMapper.readValue(jsonString, ActivityStreamDistance.class);

        //then
        assertThat(distance).isNotNull();
        assertThat(distance.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(distance.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(distance.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < distance.getData().size(); i++) {
            Integer objectValue = distance.getData().get(i);
            Integer jsonValue = dataArray.get(i).asInt();
            assertThat(objectValue).isEqualTo(jsonValue);
        }
    }

    @Test
    public void jsonToObject_heartrate() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"heartrate\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [120,121,127,130,135]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamHeartrate heartrateStream = objectMapper.readValue(jsonString, ActivityStreamHeartrate.class);

        //then
        assertThat(heartrateStream).isNotNull();
        assertThat(heartrateStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(heartrateStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(heartrateStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < heartrateStream.getData().size(); i++) {
            Integer objectValue = heartrateStream.getData().get(i);
            Integer jsonValue = dataArray.get(i).asInt();
            assertThat(objectValue).isEqualTo(jsonValue);
        }
    }

    @Test
    public void jsonToObject_watts() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"watts\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [120,121,127,130,135]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamWatts wattsStream = objectMapper.readValue(jsonString, ActivityStreamWatts.class);

        //then
        assertThat(wattsStream).isNotNull();
        assertThat(wattsStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(wattsStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(wattsStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < wattsStream.getData().size(); i++) {
            Integer objectValue = wattsStream.getData().get(i);
            Integer jsonValue = dataArray.get(i).asInt();
            assertThat(objectValue).isEqualTo(jsonValue);
        }
    }

    @Test
    public void jsonToObject_moving() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"moving\",\n" +
                "    \"original_size\": 4,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [true,false,false,true]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamMoving movingStream = objectMapper.readValue(jsonString, ActivityStreamMoving.class);

        //then
        assertThat(movingStream).isNotNull();
        assertThat(movingStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(movingStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(movingStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < movingStream.getData().size(); i++) {
            Boolean objectValue = movingStream.getData().get(i);
            boolean jsonValue = dataArray.get(i).asBoolean();
            assertThat(objectValue).isEqualTo(jsonValue);
        }
    }

    @Test
    public void jsonToObject_cadence() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"cadence\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [66,68,72,80,90]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamCadence cadenceStream = objectMapper.readValue(jsonString, ActivityStreamCadence.class);

        //then
        assertThat(cadenceStream).isNotNull();
        assertThat(cadenceStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(cadenceStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(cadenceStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < cadenceStream.getData().size(); i++) {
            Integer objectValue = cadenceStream.getData().get(i);
            int jsonValue = dataArray.get(i).asInt();
            assertThat(objectValue).isEqualTo(jsonValue);
        }
    }

    @Test
    public void jsonToObject_velocitySmooth() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"velocity_smooth\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [6.6,6.7,6.9,7.7,8.8]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamVelocitySmooth velocitySmoothStream = objectMapper.readValue(jsonString, ActivityStreamVelocitySmooth.class);

        //then
        assertThat(velocitySmoothStream).isNotNull();
        assertThat(velocitySmoothStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(velocitySmoothStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(velocitySmoothStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < velocitySmoothStream.getData().size(); i++) {
            Float objectValue = velocitySmoothStream.getData().get(i);
            double jsonValue = dataArray.get(i).asDouble();
            assertThat(objectValue).isEqualTo((float)jsonValue);
        }
    }

    @Test
    public void jsonToObject_time() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"time\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [1,2,3,4,5]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamTime timeSmoothStream = objectMapper.readValue(jsonString, ActivityStreamTime.class);

        //then
        assertThat(timeSmoothStream).isNotNull();
        assertThat(timeSmoothStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(timeSmoothStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(timeSmoothStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < timeSmoothStream.getData().size(); i++) {
            Integer objectValue = timeSmoothStream.getData().get(i);
            int jsonValue = dataArray.get(i).asInt();
            assertThat(objectValue).isEqualTo(jsonValue);
        }
    }

    @Test
    public void jsonToObject_altitude() throws JsonProcessingException {
        //given
        String jsonString = "{\n" +
                "    \"series_type\": \"altitude\",\n" +
                "    \"original_size\": 5,\n" +
                "    \"resolution\": \"high\",\n" +
                "    \"data\": [6.6,6.7,6.9,7.7,8.8]\n" +
                "}";

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
        ActivityStreamAltitude altitudeStream = objectMapper.readValue(jsonString, ActivityStreamAltitude.class);

        //then
        assertThat(altitudeStream).isNotNull();
        assertThat(altitudeStream.getOriginalSize()).isEqualTo(jsonNode.get("original_size").asLong());
        assertThat(altitudeStream.getSeriesType().getValue()).isEqualTo(jsonNode.get("series_type").asText());
        assertThat(altitudeStream.getResolution().getValue()).isEqualTo(jsonNode.get("resolution").asText());

        ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
        for(int i = 0; i < altitudeStream.getData().size(); i++) {
            Float objectValue = altitudeStream.getData().get(i);
            double jsonValue = dataArray.get(i).asDouble();
            assertThat(objectValue).isEqualTo((float)jsonValue);
        }
    }
}