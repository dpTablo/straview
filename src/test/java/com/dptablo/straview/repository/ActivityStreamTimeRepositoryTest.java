package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStreamTime;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ActivityStreamTimeRepositoryTest {
    @Autowired
    private ActivityStreamTimeRepository timeStreamRepository;

    @Test
    public void save_time() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamTime timeStream = ActivityStreamTime.builder()
                .type(ActivityStreamType.TIME)
                .summaryActivity(activity)
                .originalSize(4L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(1, 2, 3, 4))
                .build();

        //when
        ActivityStreamTime savedCadenceStream = timeStreamRepository.save(timeStream);
        ActivityStreamTime foundCadenceStream = timeStreamRepository.findById(savedCadenceStream.getStreamId())
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
    public void findBySummaryActivity() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(383284L)
                .build();

        ActivityStreamTime timeStream = ActivityStreamTime.builder()
                .type(ActivityStreamType.TIME)
                .summaryActivity(activity)
                .originalSize(4L)
                .seriesType(ActivityStreamType.DISTANCE)
                .resolution(ActivityStreamResolution.HIGH)
                .data(Arrays.asList(1, 2, 3, 4))
                .build();

        ActivityStreamTime savedTimeStream = timeStreamRepository.save(timeStream);
        assertThat(savedTimeStream).isEqualTo(timeStream);

        ActivityStreamTime foundTimeStream = timeStreamRepository.findBySummaryActivity(activity).orElseThrow(NullPointerException::new);
        assertThat(foundTimeStream).isEqualTo(timeStream);
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
}