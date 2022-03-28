package com.dptablo.straview.service;

import com.dptablo.straview.dto.entity.*;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityPowerAnalysisServiceTest {
    @InjectMocks
    private ActivityPowerAnalysisService powerAnalysisService;

    @Spy
    private ActivityPowerInfoRepository powerInfoRepository;

    @Mock
    private SummaryActivityRepository activityRepository;

    @Mock
    private ActivityStreamTimeRepository timeStreamRepository;

    @Mock
    private ActivityStreamWattsRepository wattsStreamRepository;

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

    @Test
    public void analyze() throws Exception {
        //given
        StravaAthlete athlete = StravaAthlete.builder().build();

        SummaryActivity activity = SummaryActivity.builder()
                .manageId(283842L)
                .activityId(539L)
                .ftp(208)
                .build();
        given(activityRepository.findById(activity.getManageId())).willReturn(Optional.of(activity));

        File jsonFile = Paths.get("src/test/java/com/dptablo/straview/service/activity_stream_sample.json").toFile();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(jsonFile, JsonNode.class);

        JsonNode timeNode = jsonNode.get(ActivityStreamType.TIME.getValue());
        JsonNode wattsNode = jsonNode.get(ActivityStreamType.WATTS.getValue());

        ActivityStreamTime timeStream = objectMapper.readValue(timeNode.toString(), ActivityStreamTime.class);
        ActivityStreamWatts wattsStream = objectMapper.readValue(wattsNode.toString(), ActivityStreamWatts.class);

        given(powerInfoRepository.save(any(ActivityPowerInfo.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(timeStreamRepository.findBySummaryActivity(activity)).willReturn(Optional.ofNullable(timeStream));
        given(wattsStreamRepository.findBySummaryActivity(activity)).willReturn(Optional.ofNullable(wattsStream));

        //when
        ActivityPowerInfo powerInfo = powerAnalysisService.analyze(activity.getManageId())
                .orElseThrow(NullPointerException::new);

        //then
        verify(powerInfoRepository, times(1)).save(any(ActivityPowerInfo.class));
        assertThat(powerInfo.getActivity()).isEqualTo(activity);
        assertThat(powerInfo.getFtp()).isEqualTo(activity.getFtp());
        assertThat(powerInfo.getMax()).isEqualTo(173);
        assertThat(powerInfo.getAverage()).isEqualTo(130);
        assertThat(powerInfo.getWeightedAverage()).isEqualTo(131);
        assertThat(powerInfo.getTrainingIntensity()).isEqualTo(0.63f);
        assertThat(powerInfo.getTrainingScore()).isEqualTo(20);
        assertThat(powerInfo.getKilojoules()).isEqualTo(237);
        assertThat(powerInfo.getElapsedTime()).isEqualTo(1826);

        assertThat(powerInfo.getZ1Percent()).isEqualTo(10.78f);
        assertThat(powerInfo.getZ2Percent()).isEqualTo(88.89f);
        assertThat(powerInfo.getZ3Percent()).isEqualTo(0.33f);
        assertThat(powerInfo.getZ4Percent()).isEqualTo(0);
        assertThat(powerInfo.getZ5Percent()).isEqualTo(0);
        assertThat(powerInfo.getZ6Percent()).isEqualTo(0);
        assertThat(powerInfo.getZ7Percent()).isEqualTo(0);

        assertThat(powerInfo.getZ1Seconds()).isEqualTo(197);
        assertThat(powerInfo.getZ2Seconds()).isEqualTo(1624);
        assertThat(powerInfo.getZ3Seconds()).isEqualTo(6);
        assertThat(powerInfo.getZ4Seconds()).isEqualTo(0);
        assertThat(powerInfo.getZ5Seconds()).isEqualTo(0);
        assertThat(powerInfo.getZ6Seconds()).isEqualTo(0);
        assertThat(powerInfo.getZ7Seconds()).isEqualTo(0);

        assertThat(powerInfo.getZ1Max()).isEqualTo(114);
        assertThat(powerInfo.getZ2Max()).isEqualTo(156);
        assertThat(powerInfo.getZ3Max()).isEqualTo(187);
        assertThat(powerInfo.getZ4Max()).isEqualTo(218);
        assertThat(powerInfo.getZ5Max()).isEqualTo(250);
        assertThat(powerInfo.getZ6Max()).isEqualTo(312);
        assertThat(powerInfo.getZ7Min()).isEqualTo(313);
    }
}