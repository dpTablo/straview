package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.ActivityPowerInfo;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.SummaryActivity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ActivityPowerInfoRepositoryTest {
    @Autowired
    private ActivityPowerInfoRepository powerInfoRepository;

    @Test
    public void save() {
        //given
        SummaryActivity activity = SummaryActivity.builder()
                .activityId(3838L)
                .build();

        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(390209L)
                .build();

        ActivityPowerInfo powerInfo = ActivityPowerInfo.builder()
                .activity(activity)
                .max(500)
                .average(134)
                .weightedAverage(174)
                .trainingIntensity(0.84f)
                .trainingScore(71)
                .ftp(208)
                .kilojoules(541)
                .z1Percent(11.0F)
                .z2Percent(11.0F)
                .z3Percent(11.0F)
                .z4Percent(11.0F)
                .z5Percent(11.0F)
                .z6Percent(11.0F)
                .z7Percent(11.0F)
                .z1Seconds(1313)
                .z2Seconds(1402)
                .z3Seconds(246)
                .z4Seconds(392)
                .z5Seconds(55)
                .z6Seconds(20)
                .z7Seconds(249)
                .z1Max(100)
                .z2Max(130)
                .z3Max(160)
                .z4Max(190)
                .z5Max(230)
                .z6Max(260)
                .z7Min(290)
                .elapsedTime(1800)
                .build();

        //when
        ActivityPowerInfo savedPowerInfo = powerInfoRepository.save(powerInfo);
        ActivityPowerInfo foundPowerInfo = powerInfoRepository.findById(powerInfo.getActivity().getManageId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundPowerInfo).isEqualTo(powerInfo);
        assertThat(foundPowerInfo.getMax()).isEqualTo(powerInfo.getMax());
        assertThat(foundPowerInfo.getAverage()).isEqualTo(powerInfo.getAverage());
        assertThat(foundPowerInfo.getWeightedAverage()).isEqualTo(powerInfo.getWeightedAverage());
        assertThat(foundPowerInfo.getTrainingIntensity()).isEqualTo(powerInfo.getTrainingIntensity());
        assertThat(foundPowerInfo.getTrainingScore()).isEqualTo(powerInfo.getTrainingScore());
        assertThat(foundPowerInfo.getFtp()).isEqualTo(powerInfo.getFtp());
        assertThat(foundPowerInfo.getKilojoules()).isEqualTo(powerInfo.getKilojoules());
        assertThat(foundPowerInfo.getZ1Percent()).isEqualTo(powerInfo.getZ1Percent());
        assertThat(foundPowerInfo.getZ2Percent()).isEqualTo(powerInfo.getZ2Percent());
        assertThat(foundPowerInfo.getZ3Percent()).isEqualTo(powerInfo.getZ3Percent());
        assertThat(foundPowerInfo.getZ4Percent()).isEqualTo(powerInfo.getZ4Percent());
        assertThat(foundPowerInfo.getZ5Percent()).isEqualTo(powerInfo.getZ5Percent());
        assertThat(foundPowerInfo.getZ6Percent()).isEqualTo(powerInfo.getZ6Percent());
        assertThat(foundPowerInfo.getZ7Percent()).isEqualTo(powerInfo.getZ7Percent());
        assertThat(foundPowerInfo.getZ1Seconds()).isEqualTo(powerInfo.getZ1Seconds());
        assertThat(foundPowerInfo.getZ2Seconds()).isEqualTo(powerInfo.getZ2Seconds());
        assertThat(foundPowerInfo.getZ3Seconds()).isEqualTo(powerInfo.getZ3Seconds());
        assertThat(foundPowerInfo.getZ4Seconds()).isEqualTo(powerInfo.getZ4Seconds());
        assertThat(foundPowerInfo.getZ5Seconds()).isEqualTo(powerInfo.getZ5Seconds());
        assertThat(foundPowerInfo.getZ6Seconds()).isEqualTo(powerInfo.getZ6Seconds());
        assertThat(foundPowerInfo.getZ7Seconds()).isEqualTo(powerInfo.getZ7Seconds());
        assertThat(foundPowerInfo.getZ1Max()).isEqualTo(powerInfo.getZ1Max());
        assertThat(foundPowerInfo.getZ2Max()).isEqualTo(powerInfo.getZ2Max());
        assertThat(foundPowerInfo.getZ3Max()).isEqualTo(powerInfo.getZ3Max());
        assertThat(foundPowerInfo.getZ4Max()).isEqualTo(powerInfo.getZ4Max());
        assertThat(foundPowerInfo.getZ5Max()).isEqualTo(powerInfo.getZ5Max());
        assertThat(foundPowerInfo.getZ6Max()).isEqualTo(powerInfo.getZ6Max());
        assertThat(foundPowerInfo.getZ7Min()).isEqualTo(powerInfo.getZ7Min());
        assertThat(foundPowerInfo.getElapsedTime()).isEqualTo(powerInfo.getElapsedTime());
    }
}