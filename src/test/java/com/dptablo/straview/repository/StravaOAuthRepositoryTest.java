package com.dptablo.straview.repository;

import com.dptablo.straview.dto.StravaAthlete;
import com.dptablo.straview.dto.StravaOAuthTokenInfo;
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
        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(29309L)
                .tokenType("Bearer")
                .expiresAt(1568775134)
                .expiresIn(20000)
                .refreshToken("lkdfslkj4sdf838")
                .accessToken("gfdklfdlgk980ff3")
                .build();

        assertThatNoException().isThrownBy(() -> repository.save(tokenInfo));

        Optional<StravaOAuthTokenInfo> foundTokenInfo = repository.findById(tokenInfo.getAthleteId());
        assertThat(foundTokenInfo.isPresent()).isTrue();
        assertThat(foundTokenInfo.get().getAthleteId()).isEqualTo(tokenInfo.getAthleteId());
    }

    @Test
    public void athlete() {
        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(29309L)
                .tokenType("Bearer")
                .expiresAt(1568775134)
                .expiresIn(20000)
                .refreshToken("lkdfslkj4sdf838")
                .accessToken("gfdklfdlgk980ff3")
                .build();

        StravaAthlete athlete = StravaAthlete.builder()
                .id(tokenInfo.getAthleteId())
                .userName("dptablo")
                .resourceState(1)
                .build();

        assertThatNoException().isThrownBy(() -> {
            athleteRepository.save(athlete);

            StravaOAuthTokenInfo returnedTokenInfo = repository.save(tokenInfo);
            returnedTokenInfo.setAthlete(athlete);
        });

        Optional<StravaOAuthTokenInfo> foundTokenInfoOptional = repository.findById(tokenInfo.getAthleteId());
        StravaOAuthTokenInfo foundTokenInfo = foundTokenInfoOptional.orElseThrow(NullPointerException::new);
        StravaAthlete foundAthlete = foundTokenInfo.getAthlete();

        assertThat(foundAthlete.getId()).isEqualTo(tokenInfo.getAthleteId());
        assertThat(foundAthlete.getUserName()).isEqualTo(athlete.getUserName());
        assertThat(foundAthlete.getResourceState()).isEqualTo(athlete.getResourceState());
    }
}
