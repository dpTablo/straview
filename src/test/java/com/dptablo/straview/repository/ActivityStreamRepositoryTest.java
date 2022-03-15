package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityStream;
import com.dptablo.straview.dto.entity.ActivityStreamDistance;
import com.dptablo.straview.dto.entity.ActivityStreamPK;
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
public class ActivityStreamRepositoryTest {
    @Autowired
    private ActivityStreamRepository<ActivityStreamDistance> activityStreamDistanceRepository;

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
        activityStreamDistanceRepository.save(distanceStream);
        ActivityStreamPK pk = new ActivityStreamPK(distanceStream.getType(), distanceStream.getSummaryActivity().getActivityId());
        ActivityStreamDistance foundDistanceStream = activityStreamDistanceRepository.findById(pk)
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
}