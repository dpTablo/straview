package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.StravaOAuthTokenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class StravaOAuthRepositoryTest {
    @Autowired
    private StravaOAuthRepository repository;

    @Test
    public void save() {
        //given
        Long athleteId = 23823984L;

        StravaOAuthTokenInfo tokenInfo = StravaOAuthTokenInfo.builder()
                .athleteId(athleteId)
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
}
