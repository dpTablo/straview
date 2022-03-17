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
}