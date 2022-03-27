package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStreamWatts;
import com.dptablo.straview.dto.entity.SummaryActivity;
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
class ActivityStreamWattsRepositoryTest {
    @Autowired
    private ActivityStreamWattsRepository wattsStreamRepository;

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
        ActivityStreamWatts savedWattsStream = wattsStreamRepository.save(wattsStream);
        ActivityStreamWatts foundWattsStream = wattsStreamRepository.findById(savedWattsStream.getStreamId())
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
    public void findBySummaryActivity() {
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
        ActivityStreamWatts savedWattsStream = wattsStreamRepository.save(wattsStream);
        ActivityStreamWatts foundWattsStream = wattsStreamRepository.findBySummaryActivity(activity)
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(savedWattsStream).isEqualTo(wattsStream);
        assertThat(foundWattsStream).isEqualTo(wattsStream);
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