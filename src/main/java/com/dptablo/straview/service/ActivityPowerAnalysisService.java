package com.dptablo.straview.service;

import com.dptablo.straview.dto.entity.ActivityPowerInfo;
import com.dptablo.straview.dto.entity.ActivityStreamTime;
import com.dptablo.straview.dto.entity.ActivityStreamWatts;
import com.dptablo.straview.dto.entity.SummaryActivity;
import com.dptablo.straview.repository.ActivityPowerInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityPowerAnalysisService {
    private final ActivityPowerInfoRepository powerInfoRepository;

    public Optional<ActivityPowerInfo> analysis(SummaryActivity activity, ActivityStreamTime timeStream, ActivityStreamWatts wattsStream) {
        try {
            ActivityPowerInfo powerInfo = ActivityPowerInfo.builder()
                    .activity(activity)
                    .ftp(activity.getFtp())
                    .build();

            calculateElapsedTime(powerInfo, timeStream);
            calculateMax(powerInfo, wattsStream);
            calculateAveragePower(powerInfo, wattsStream);
            calculateWeightedAveragePower(powerInfo, wattsStream);
            calculateTrainingIntensity(powerInfo);
            calculateTrainingScore(powerInfo);
            calculateKilojoules(powerInfo);
            calculatePowerZoneValue(powerInfo);
            calculatePowerZonePercentageAndTime(powerInfo, wattsStream);
            return Optional.ofNullable(powerInfoRepository.save(powerInfo));
        } catch(Throwable e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    private void calculateElapsedTime(ActivityPowerInfo powerInfo, ActivityStreamTime timeStream) {
        List<Integer> timeStreamData = timeStream.getData();
        int elapsedTime = timeStreamData.get(timeStreamData.size() - 1);
        powerInfo.setElapsedTime(elapsedTime);
    }

    private void calculateMax(ActivityPowerInfo powerInfo, ActivityStreamWatts wattsStream) {
        List<Integer> watts = wattsStream.getData();
        int max = watts.stream().mapToInt(v -> v).max()
                .orElseThrow(NullPointerException::new);
        powerInfo.setMax(max);
    }

    private void calculatePowerZoneValue(ActivityPowerInfo powerInfo) {
    /*
        COGGAN POWER ZONES
        z1 : < 55%
        z2 : 56~75%
        z3 : 76~90%
        z4 : 91~105%
        z5 : 106~120%
        z6 : 121~150%
        z7 : 151%~
     */
        long z1Max = Math.round(powerInfo.getFtp() * 0.55);
        long z2Max = Math.round(powerInfo.getFtp() * 0.75);
        long z3Max = Math.round(powerInfo.getFtp() * 0.90);
        long z4Max = Math.round(powerInfo.getFtp() * 1.05);
        long z5Max = Math.round(powerInfo.getFtp() * 1.20);
        long z6Max = Math.round(powerInfo.getFtp() * 1.50);
        long z7Min = z6Max + 1;

        powerInfo.setZ1Max(Math.toIntExact(z1Max));
        powerInfo.setZ2Max(Math.toIntExact(z2Max));
        powerInfo.setZ3Max(Math.toIntExact(z3Max));
        powerInfo.setZ4Max(Math.toIntExact(z4Max));
        powerInfo.setZ5Max(Math.toIntExact(z5Max));
        powerInfo.setZ6Max(Math.toIntExact(z6Max));
        powerInfo.setZ7Min(Math.toIntExact(z7Min));
    }

    private void calculateTrainingScore(ActivityPowerInfo powerInfo) {
        int trainingScore = Math.round(
                ((powerInfo.getElapsedTime() * powerInfo.getAverage() * powerInfo.getTrainingIntensity())
                        / (3600 * powerInfo.getFtp())
                ) * 100
        );
        powerInfo.setTrainingScore(trainingScore);
    }

    private void calculateAveragePower(ActivityPowerInfo powerInfo, ActivityStreamWatts wattsStream) {
        double averagePower = wattsStream.getData().stream()
                .mapToInt(v -> v).average()
                    .orElseThrow(NullPointerException::new);

        int roundAveragePower = Math.toIntExact(Math.round(averagePower));
        powerInfo.setAverage(roundAveragePower);
    }

    private void calculateTrainingIntensity(ActivityPowerInfo powerInfo) {
        BigDecimal trainingIntensity = new BigDecimal(powerInfo.getWeightedAverage() / (float)powerInfo.getFtp());
        trainingIntensity = trainingIntensity.setScale(2, RoundingMode.HALF_UP);
        powerInfo.setTrainingIntensity(trainingIntensity.floatValue());
    }

    private void calculateWeightedAveragePower(ActivityPowerInfo powerInfo, ActivityStreamWatts wattsStream) {
        /*
        Normalized Power (NP) is a metric to quantify training intensity with power data and is introduced by Andrew Coggan.
        The concept of NP is discussed in chapter 7 of the book.
        It is especially useful in conjuction with the other algorithms below.

        Step 1: Calculate the rolling average with a window of 30 seconds:
                Start at 30 seconds, calculate the average power of the previous 30 seconds and to the for every second after that.
        Step 2: Calculate the 4th power of the values from the previous step.
        Step 3: Calculate the average of the values from the previous step.
        Step 4: Take the fourth root of the average from the previous step. This is your normalized power.

        Or in pseudo code:

        rolling_average = 30 second rolling average
        rolling_avg_powered = rolling_average^4
        avg_powered_values = average of rolling_avg_powered
        NP = avg_powered_values^0.25

        The unit of NP is Watt.
        */

        int ROLLING_SECONDS = 60;
        Integer[] wattsArray = wattsStream.getData().toArray(new Integer[0]);

        double rollingAverage = IntStream.range(0, ROLLING_SECONDS - 1)
                .mapToLong(i -> wattsArray[i])
                .average()
                .orElseThrow(NullPointerException::new);

        List<BigDecimal> rollingAverage4thPowerList = new ArrayList<>();

        int frontPointer = 0;
        for(int rearPointer = ROLLING_SECONDS; rearPointer < wattsArray.length; rearPointer++) {
            rollingAverage = ((rollingAverage * ROLLING_SECONDS) - wattsArray[frontPointer] + wattsArray[rearPointer])
                    / ROLLING_SECONDS;

            BigDecimal value = new BigDecimal(Math.pow(rollingAverage, 4));
            value = value.setScale(4, RoundingMode.HALF_UP);
            rollingAverage4thPowerList.add(value);

            frontPointer++;
        }

        BigDecimal rollingAverage4thPowerAverage = rollingAverage4thPowerList.stream()
                .reduce((v1, v2) -> v1.add(v2))
                .map(sum -> sum.divide(
                        BigDecimal.valueOf(rollingAverage4thPowerList.size()), 4, RoundingMode.HALF_UP)
                )
                .orElseThrow(NullPointerException::new);

        int weightedAverage = Math.toIntExact(
                Math.round(Math.pow(rollingAverage4thPowerAverage.doubleValue(), 0.25))
        );
        powerInfo.setWeightedAverage(weightedAverage);
    }

    private void calculateKilojoules(ActivityPowerInfo powerInfo) {
        int kilojoules = Math.round((powerInfo.getAverage() * powerInfo.getElapsedTime()) / 1000);
        powerInfo.setKilojoules(kilojoules);
    }

    private void calculatePowerZonePercentageAndTime(ActivityPowerInfo powerInfo, ActivityStreamWatts wattsStream) {
        List<Integer> watts = wattsStream.getData();

        int totalCount = watts.size();
        int z1Count = getPowerZoneDataCount(-1, powerInfo.getZ1Max(), watts);
        int z2Count = getPowerZoneDataCount(powerInfo.getZ1Max(), powerInfo.getZ2Max(), watts);
        int z3Count = getPowerZoneDataCount(powerInfo.getZ2Max(), powerInfo.getZ3Max(), watts);
        int z4Count = getPowerZoneDataCount(powerInfo.getZ3Max(), powerInfo.getZ4Max(), watts);
        int z5Count = getPowerZoneDataCount(powerInfo.getZ4Max(), powerInfo.getZ5Max(), watts);
        int z6Count = getPowerZoneDataCount(powerInfo.getZ5Max(), powerInfo.getZ6Max(), watts);
        int z7Count = getPowerZoneDataCount(powerInfo.getZ7Min(), Integer.MAX_VALUE, watts);

        powerInfo.setZ1Seconds(z1Count);
        powerInfo.setZ2Seconds(z2Count);
        powerInfo.setZ3Seconds(z3Count);
        powerInfo.setZ4Seconds(z4Count);
        powerInfo.setZ5Seconds(z5Count);
        powerInfo.setZ6Seconds(z6Count);
        powerInfo.setZ7Seconds(z7Count);

        powerInfo.setZ1Percent(createPowerZonePercentage(z1Count, totalCount, 2));
        powerInfo.setZ2Percent(createPowerZonePercentage(z2Count, totalCount, 2));
        powerInfo.setZ3Percent(createPowerZonePercentage(z3Count, totalCount, 2));
        powerInfo.setZ4Percent(createPowerZonePercentage(z4Count, totalCount, 2));
        powerInfo.setZ5Percent(createPowerZonePercentage(z5Count, totalCount, 2));
        powerInfo.setZ6Percent(createPowerZonePercentage(z6Count, totalCount, 2));
        powerInfo.setZ7Percent(createPowerZonePercentage(z7Count, totalCount, 2));
    }

    private int getPowerZoneDataCount(int min, int max, List<Integer> watts) {
        return Math.toIntExact(
                watts.stream().filter(value -> value <= max && value > min).count()
        );
    }

    private float createPowerZonePercentage(int powerDataCount, int totalDataCount, int scale) {
        return new BigDecimal((powerDataCount / (float)totalDataCount) * 100.0)
                .setScale(scale, RoundingMode.HALF_UP)
                .floatValue();
    }
}
