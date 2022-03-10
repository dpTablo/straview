package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.StravaAthlete;
import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@ActiveProfiles("test")
public class StravaOAuthRepositoryTest {
    @Autowired
    private StravaOAuthRepository repository;

    @Autowired
    private StravaAthleteRepository athleteRepository;

    @Test
    public void save() {
        //given
        Long athleteId = 23823984L;

        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(athleteId)
                .userName("dptablo")
                .resourceState(1)
                .build();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athlete(athlete)
                .tokenType("Bearer")
                .expiresAt(1568775134L)
                .expiresIn(20000L)
                .refreshToken("lkdfslkj4sdf838")
                .accessToken("gfdklfdlgk980ff3")
                .build();

        //when
        repository.save(tokenInfo);

        //then
        StravaOAuthTokenInfo foundTokenInfo = repository.findById(athleteId).orElseThrow(NullPointerException::new);
        assertThat(foundTokenInfo).isEqualTo(tokenInfo);
    }

    @Test
    public void athlete() {
        //given
        Long athleteId = 29309L;

        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(athleteId)
                .userName("dptablo")
                .resourceState(1)
                .build();

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athlete(athlete)
                .tokenType("Bearer")
                .expiresAt(1568775134L)
                .expiresIn(20000L)
                .refreshToken("lkdfslkj4sdf838")
                .accessToken("gfdklfdlgk980ff3")
                .build();

        //when
        repository.save(tokenInfo);

        //then
        StravaOAuthTokenInfo foundTokenInfo = repository.findById(athleteId).orElseThrow(NullPointerException::new);
        StravaAthlete foundAthlete = foundTokenInfo.getAthlete();
        assertThat(foundAthlete.getAthleteId()).isEqualTo(athleteId);
    }
}
