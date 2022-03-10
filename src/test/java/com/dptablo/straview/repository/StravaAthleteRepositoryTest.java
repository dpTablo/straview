package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.Gear;
import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class StravaAthleteRepositoryTest {
    @Autowired
    private StravaAthleteRepository athleteRepository;

    @Autowired
    private StravaOAuthRepository stravaOAuthRepository;

    @Autowired
    private GearRepository gearRepository;

    @Test
    public void save() {
        //given
        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(1123213L)
                .resourceState(1)
                .firstName("LEE")
                .lastName("YOUNG WOO")
                .userName("dpTablo")
                .profileMedium("http://~~~/a.jpg")
                .profile("http://~~~/b.jpg")
                .city("Seoul")
                .state("Seoul State")
                .country("KOREA")
                .sex("M")
                .premium(true)
                .summit(true)
                .createdAt("2020-11-14T02:30:05Z")
                .updatedAt("2021-11-14T05:45:03Z")
                .followerCount(5)
                .friendCount(2)
                .measurementPreference("meters")
                .ftp(193)
                .weight(71.0f)
                .badgeTypeId(2)
                .athleteType(2)
                .datePreference("%m/%d/%Y")
                .build();

        Gear gear1 = Gear.builder()
                .gearId("b11")
                .athlete(athlete)
                .name("2021 MERIDA reacto 4000")
                .primaryFlag(true)
                .distance(3889259f)
                .convertedDistance(3889.3f)
                .resourceState(2)
                .build();

        Gear gear2 = Gear.builder()
                .gearId("b22")
                .athlete(athlete)
                .name("2021 Trek Domane AL3")
                .primaryFlag(false)
                .distance(433263f)
                .convertedDistance(433.3f)
                .resourceState(2)
                .build();

        athlete.setBikes(Arrays.asList(gear1, gear2));

        //when
        assertThatNoException().isThrownBy(() -> athleteRepository.save(athlete));

        //then
        StravaAthlete foundAthlete = athleteRepository.findById(athlete.getAthleteId()).orElseThrow(NullPointerException::new);
        assertThat(foundAthlete.getAthleteId()).isEqualTo(athlete.getAthleteId());
        assertThat(foundAthlete.getResourceState()).isEqualTo(athlete.getResourceState());
        assertThat(foundAthlete.getFirstName()).isEqualTo(athlete.getFirstName());
        assertThat(foundAthlete.getLastName()).isEqualTo(athlete.getLastName());
        assertThat(foundAthlete.getProfileMedium()).isEqualTo(athlete.getProfileMedium());
        assertThat(foundAthlete.getProfile()).isEqualTo(athlete.getProfile());
        assertThat(foundAthlete.getCity()).isEqualTo(athlete.getCity());
        assertThat(foundAthlete.getState()).isEqualTo(athlete.getState());
        assertThat(foundAthlete.getCountry()).isEqualTo(athlete.getCountry());
        assertThat(foundAthlete.getSex()).isEqualTo(athlete.getSex());
        assertThat(foundAthlete.getPremium()).isEqualTo(athlete.getPremium());
        assertThat(foundAthlete.getSummit()).isEqualTo(athlete.getSummit());
        assertThat(foundAthlete.getCreatedAt()).isEqualTo(athlete.getCreatedAt());
        assertThat(foundAthlete.getUpdatedAt()).isEqualTo(athlete.getUpdatedAt());
        assertThat(foundAthlete.getFollowerCount()).isEqualTo(athlete.getFollowerCount());
        assertThat(foundAthlete.getFriendCount()).isEqualTo(athlete.getFriendCount());
        assertThat(foundAthlete.getFtp()).isEqualTo(athlete.getFtp());
        assertThat(foundAthlete.getWeight()).isEqualTo(athlete.getWeight());
        assertThat(foundAthlete.getBadgeTypeId()).isEqualTo(athlete.getBadgeTypeId());
        assertThat(foundAthlete.getAthleteType()).isEqualTo(athlete.getAthleteType());
        assertThat(foundAthlete.getDatePreference()).isEqualTo(athlete.getDatePreference());

        List<Gear> bikes = foundAthlete.getBikes();
        assertThat(bikes.size()).isEqualTo(2);
        assertThat(bikes.get(0).getGearId()).isEqualTo(gear1.getGearId());
        assertThat(bikes.get(1).getGearId()).isEqualTo(gear2.getGearId());
    }

    @Test
    public void stravaOAuthTokenInfo() {
        //given
        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(1123213L)
                .userName("dptablo")
                .resourceState(1)
                .build();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athlete(athlete)
                .tokenType("Bearer")
                .expiresAt(1568775134L)
                .expiresIn(21600L)
                .refreshToken("kldfaskl43k2dddd")
                .accessToken("f345908sdf453klf")
                .build();

        athlete.setStravaOAuthTokenInfo(tokenInfo);

        //when
        athleteRepository.save(athlete);

        //then
        StravaAthlete foundAthlete = athleteRepository.findById(athlete.getAthleteId())
                .orElseThrow(NullPointerException::new);
        StravaOAuthTokenInfo foundTakenInfo = foundAthlete.getStravaOAuthTokenInfo();

        assertThat(foundTakenInfo.getAthlete()).isEqualTo(foundAthlete);
        assertThat(foundTakenInfo.getTokenType()).isEqualTo(tokenInfo.getTokenType());
        assertThat(foundTakenInfo.getExpiresAt()).isEqualTo(tokenInfo.getExpiresAt());
        assertThat(foundTakenInfo.getExpiresIn()).isEqualTo(tokenInfo.getExpiresIn());
        assertThat(foundTakenInfo.getRefreshToken()).isEqualTo(tokenInfo.getRefreshToken());
        assertThat(foundTakenInfo.getAccessToken()).isEqualTo(tokenInfo.getAccessToken());
    }
}