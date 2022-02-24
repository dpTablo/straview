package com.dptablo.straview.repository;

import com.dptablo.straview.dto.StravaAthlete;
import com.dptablo.straview.dto.StravaOAuthTokenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class StravaAthleteRepositoryTest {
    @Autowired
    private StravaAthleteRepository repository;

    @Autowired
    private StravaOAuthRepository stravaOAuthRepository;

    @Test
    public void save() {
        StravaAthlete athlete = StravaAthlete.builder()
                .id(1123213L)
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
                .build();

        assertThatNoException().isThrownBy(() -> repository.save(athlete));

        Optional<StravaAthlete> foundAthlete = repository.findById(athlete.getId());
        assertThat(foundAthlete.isPresent()).isTrue();
        assertThat(foundAthlete.get().getId()).isEqualTo(athlete.getId());
        assertThat(foundAthlete.get().getResourceState()).isEqualTo(athlete.getResourceState());
        assertThat(foundAthlete.get().getFirstName()).isEqualTo(athlete.getFirstName());
        assertThat(foundAthlete.get().getLastName()).isEqualTo(athlete.getLastName());
        assertThat(foundAthlete.get().getProfileMedium()).isEqualTo(athlete.getProfileMedium());
        assertThat(foundAthlete.get().getProfile()).isEqualTo(athlete.getProfile());
        assertThat(foundAthlete.get().getCity()).isEqualTo(athlete.getCity());
        assertThat(foundAthlete.get().getState()).isEqualTo(athlete.getState());
        assertThat(foundAthlete.get().getCountry()).isEqualTo(athlete.getCountry());
        assertThat(foundAthlete.get().getSex()).isEqualTo(athlete.getSex());
        assertThat(foundAthlete.get().getPremium()).isEqualTo(athlete.getPremium());
        assertThat(foundAthlete.get().getSummit()).isEqualTo(athlete.getSummit());
        assertThat(foundAthlete.get().getCreatedAt()).isEqualTo(athlete.getCreatedAt());
        assertThat(foundAthlete.get().getUpdatedAt()).isEqualTo(athlete.getUpdatedAt());
        assertThat(foundAthlete.get().getFollowerCount()).isEqualTo(athlete.getFollowerCount());
        assertThat(foundAthlete.get().getFriendCount()).isEqualTo(athlete.getFriendCount());
        assertThat(foundAthlete.get().getFtp()).isEqualTo(athlete.getFtp());
        assertThat(foundAthlete.get().getWeight()).isEqualTo(athlete.getWeight());
    }

    @Test
    public void stravaOAuthTokenInfo() {
        StravaAthlete athlete = StravaAthlete.builder()
                .id(1123213L)
                .userName("dptablo")
                .resourceState(1)
                .build();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athlete.getId())
                .tokenType("Bearer")
                .expiresAt(1568775134)
                .expiresIn(21600)
                .refreshToken("kldfaskl43k2dddd")
                .accessToken("f345908sdf453klf")
                .build();

        assertThatNoException().isThrownBy(() -> {
            stravaOAuthRepository.save(tokenInfo);

            StravaAthlete returnedAthlete = repository.save(athlete);
            returnedAthlete.setStravaOAuthTokenInfo(tokenInfo);
        });

        Optional<StravaAthlete> foundAthleteOptional = repository.findById(athlete.getId());
        StravaAthlete foundAthlete = foundAthleteOptional.orElseThrow(NullPointerException::new);
        StravaOAuthTokenInfo foundTakenInfo = foundAthlete.getStravaOAuthTokenInfo();

        assertThat(foundTakenInfo.getAthleteId()).isEqualTo(foundAthlete.getId());
        assertThat(foundTakenInfo.getTokenType()).isEqualTo(tokenInfo.getTokenType());
        assertThat(foundTakenInfo.getExpiresAt()).isEqualTo(tokenInfo.getExpiresAt());
        assertThat(foundTakenInfo.getExpiresIn()).isEqualTo(tokenInfo.getExpiresIn());
        assertThat(foundTakenInfo.getRefreshToken()).isEqualTo(tokenInfo.getRefreshToken());
        assertThat(foundTakenInfo.getAccessToken()).isEqualTo(tokenInfo.getAccessToken());
    }
}